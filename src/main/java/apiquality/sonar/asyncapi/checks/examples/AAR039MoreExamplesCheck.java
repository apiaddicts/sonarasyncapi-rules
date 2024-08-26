/*
 * SonarQube AsyncAPI Plugin
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
package apiquality.sonar.asyncapi.checks.examples;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = AAR039MoreExamplesCheck.CHECK_KEY)
public class AAR039MoreExamplesCheck extends BaseCheck {
  public static final String CHECK_KEY = "AAR039";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(AsyncApiGrammar.OPERATION);
  }

  @Override
  protected void visitNode(JsonNode node) {
    JsonNode examplesNode = node.at("/message/examples").value();
    
    // Check if examples exist and are an array
    if (examplesNode.isArray()) {
      int examplesCount = examplesNode.getJsonChildren().size();
      // Check if there are more than two examples
      if (examplesCount <= 2) {
        addIssue(CHECK_KEY, translate("AAR039.error.not.enough.examples"), node.key());
      }
    } else {
      // If no examples or not an array, it's a violation
      addIssue(CHECK_KEY, translate("AAR039.error.no.examples"), node.key());
    }
  }
}
