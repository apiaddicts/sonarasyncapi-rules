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

import java.util.List;
import java.util.Set;

@Rule(key = AAR019IDSchemasCheck.CHECK_KEY)
public class AAR019IDSchemasCheck extends BaseCheck {
  public static final String CHECK_KEY = "AAR019";
  private static final String ERROR_KEY = "AAR019.error";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(AsyncApiGrammar.SCHEMA, AsyncApiGrammar.PAYLOAD_SCHEMA);
  }

  @Override
  protected void visitNode(JsonNode node) {
    node = JsonNodeUtils.resolve(node);
    if (AvroUtils.isAvroComponentSchema(node)) {
      JsonNode inner = node.get("schema");
      if (inner == null || inner.isMissing() || inner.isNull()) return;
      node = inner;
    }
    JsonNode typeNode = node.propertyMap().get("type");
    if (typeNode == null || typeNode.isMissing() || typeNode.isNull()) {
      return;
    }

    String type = typeNode.stringValue();

    if ("object".equals(type)) {
      checkJsonSchemaObject(node);
    } else if ("record".equals(type)) {
      checkAvroRecord(node);
    }
  }

  private void checkJsonSchemaObject(JsonNode node) {
    JsonNode propertiesNode = node.propertyMap().get("properties");
    if (propertiesNode == null || propertiesNode.isMissing() || propertiesNode.isNull()) {
      addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
      return;
    }
    if (!propertiesNode.propertyMap().containsKey("id")) {
      addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
    }
  }

  private void checkAvroRecord(JsonNode node) {
    JsonNode fieldsNode = node.propertyMap().get("fields");
    if (fieldsNode == null || fieldsNode.isMissing() || fieldsNode.isNull()) {
      addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
      return;
    }
    List<JsonNode> fields = fieldsNode.elements();
    boolean hasId = fields.stream().anyMatch(field -> {
      JsonNode nameNode = field.get("name");
      return nameNode != null && !nameNode.isMissing() && "id".equals(nameNode.getTokenValue());
    });
    if (!hasId) {
      addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
    }
  }
}
