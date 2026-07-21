/*
 * SonarQube OpenAPI Plugin
 * Copyright (C) 2018-2019 Societe Generale
 * vincent.girard-reydet AT socgen DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

@Rule(key = AAR026MessageSchemasCheck.CHECK_KEY)
public class AAR026MessageSchemasCheck extends BaseCheck {
  public static final String CHECK_KEY = "AAR026";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(AsyncApiGrammar.ROOT);
  }

  @Override
  protected void visitNode(JsonNode root) {
    JsonNode channels = root.get("channels");
    if (channels.isMissing() || channels.isNull()) return;

    if (AsyncAPIVersionDetector.isVersion3Plus(root)) {
      visitV3Channels(channels);
    } else {
      visitV2Channels(channels);
    }
  }

  private void visitV3Channels(JsonNode channels) {
    for (Map.Entry<String, JsonNode> channelEntry : channels.propertyMap().entrySet()) {
      JsonNode channel = channelEntry.getValue();
      JsonNode messages = channel.get("messages");
      if (messages.isMissing() || messages.isNull()) continue;
      for (Map.Entry<String, JsonNode> msgEntry : messages.propertyMap().entrySet()) {
        JsonNode msg = msgEntry.getValue();
        // $ref nodes are parsed as REF, not MESSAGE — inline definitions have no $ref
        JsonNode ref = msg.propertyMap().get("$ref");
        if (ref == null || ref.isMissing() || ref.isNull()) {
          addIssue(CHECK_KEY, translate("AAR026.error"), msg.key());
        }
      }
    }
  }

  private void visitV2Channels(JsonNode channels) {
    for (Map.Entry<String, JsonNode> channelEntry : channels.propertyMap().entrySet()) {
      JsonNode channel = channelEntry.getValue();
      checkV2OperationMessage(channel.get("subscribe"));
      checkV2OperationMessage(channel.get("publish"));
    }
  }

  private void checkV2OperationMessage(JsonNode operation) {
    if (operation.isMissing() || operation.isNull()) return;
    JsonNode message = operation.get("message");
    if (message.isMissing() || message.isNull()) return;

    JsonNode oneOf = message.propertyMap().get("oneOf");
    if (oneOf != null && !oneOf.isMissing() && !oneOf.isNull()) {
      for (JsonNode member : oneOf.elements()) {
        checkMessageRef(member);
      }
      return;
    }
    checkMessageRef(message);
  }

  private void checkMessageRef(JsonNode message) {
    JsonNode ref = message.propertyMap().get("$ref");
    if (ref == null || ref.isMissing() || ref.isNull()) {
      addIssue(CHECK_KEY, translate("AAR026.error"), message.key());
    }
  }
}
