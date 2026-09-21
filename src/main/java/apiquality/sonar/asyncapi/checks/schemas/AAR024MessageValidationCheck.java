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
import apiquality.sonar.asyncapi.utils.AvroUtils;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.impl.MissingNode;

import java.util.HashSet;
import java.util.Set;

@Rule(key = AAR024MessageValidationCheck.CHECK_KEY)
public class AAR024MessageValidationCheck extends BaseCheck {
  public static final String CHECK_KEY = "AAR024";

  private static final int CONTENT_TYPE_ABSENT = 0;
  private static final int CONTENT_TYPE_NULL = 1;
  private static final int CONTENT_TYPE_DECLARED = 2;

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(AsyncApiGrammar.MESSAGE);
  }

  @Override
  protected void visitNode(JsonNode node) {
    validateMessage(JsonNodeUtils.resolve(node));
  }

  private void validateMessage(JsonNode message) {
    if (message == null || message.isMissing() || message.isNull()) {
      return;
    }
    JsonNode oneOf = message.get("oneOf");
    if (oneOf.isArray()) {
      for (JsonNode member : oneOf.elements()) {
        validateMessage(resolveLocal(member));
      }
      return;
    }
    if (!hasContentType(message) && !AvroUtils.isAvroMessage(message)) {
      addIssue(CHECK_KEY, translate("AAR024.error"), anchorFor(message));
    }
  }

  private static boolean hasContentType(JsonNode message) {
    int state = contentTypeState(message);
    if (state != CONTENT_TYPE_ABSENT) {
      return state == CONTENT_TYPE_DECLARED;
    }
    JsonNode traits = message.get("traits");
    if (!traits.isArray()) {
      return false;
    }
    for (JsonNode trait : traits.elements()) {
      int traitState = contentTypeState(resolveLocal(trait));
      if (traitState != CONTENT_TYPE_ABSENT) {
        state = traitState;
      }
    }
    return state == CONTENT_TYPE_DECLARED;
  }

  private static int contentTypeState(JsonNode node) {
    if (node == null || node.isMissing() || node.isNull()) {
      return CONTENT_TYPE_ABSENT;
    }
    JsonNode contentType = node.get("contentType");
    if (contentType.isMissing()) {
      return CONTENT_TYPE_ABSENT;
    }
    return contentType.isNull() ? CONTENT_TYPE_NULL : CONTENT_TYPE_DECLARED;
  }

  private static JsonNode resolveLocal(JsonNode node) {
    Set<String> visited = new HashSet<>();
    JsonNode current = node;
    while (current != null && current.isRef()) {
      String ref = current.get("$ref").getTokenValue();
      if (ref == null || !ref.startsWith("#/") || !visited.add(ref)) {
        return MissingNode.MISSING;
      }
      current = current.resolve();
    }
    return current;
  }

  private static JsonNode anchorFor(JsonNode message) {
    JsonNode keyNode = message.key();
    return (keyNode == null || keyNode.isMissing()) ? message : keyNode;
  }
}
