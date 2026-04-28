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
package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

@Rule(key = AAR043SecurityChannelCheck.CHECK_KEY)
public class AAR043SecurityChannelCheck extends BaseCheck {
  public static final String CHECK_KEY = "AAR043";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(AsyncApiGrammar.ROOT);
  }

  @Override
  protected void visitNode(JsonNode node) {
    if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
      visitV3(node);
    } else {
      visitV2(node);
    }
  }

  private void visitV3(JsonNode root) {
    JsonNode componentsNode = root.get("components");
    if (componentsNode.isMissing() || componentsNode.isNull()) {
      addIssue(CHECK_KEY, translate("AAR043.error"), componentsNode.key());
      return;
    }
    JsonNode secSchemesNode = componentsNode.get("securitySchemes");
    if (secSchemesNode.isMissing() || secSchemesNode.isNull()) {
      addIssue(CHECK_KEY, translate("AAR043.error"), secSchemesNode.key());
    }
  }

  private void visitV2(JsonNode root) {
    JsonNode channels = root.get("channels");
    if (channels.isMissing() || channels.isNull()) return;
    for (Map.Entry<String, JsonNode> entry : channels.propertyMap().entrySet()) {
      JsonNode channel = entry.getValue();
      boolean hasSecurity = operationHasSecurity(channel.get("subscribe"))
          || operationHasSecurity(channel.get("publish"));
      if (!hasSecurity) {
        addIssue(CHECK_KEY, translate("AAR043.error"), channel.key());
      }
    }
  }

  private boolean operationHasSecurity(JsonNode operation) {
    if (operation.isMissing() || operation.isNull()) return false;
    JsonNode security = operation.get("security");
    return !security.isMissing() && !security.isNull();
  }
}
