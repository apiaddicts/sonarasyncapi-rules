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
package apiquality.sonar.asyncapi.checks.format;

import com.google.common.collect.Sets;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = AAR034NumericFormatCheck.CHECK_KEY)
public class AAR034NumericFormatCheck extends AbstractSchemaPropertyCheck {
    public static final String CHECK_KEY = "AAR034";
    private static final String ERROR_KEY = "AAR034.error";
    private static final Set<String> VALID_FORMATS = Sets.newHashSet("int32", "int64", "float", "double");

    @Override
    protected void checkProperty(JsonNode property, JsonNode anchor) {
        String type = typeOf(property);
        if (!"integer".equals(type) && !"number".equals(type)) {
            return;
        }
        JsonNode formatNode = property.get("format");
        if (formatNode == null || formatNode.isMissing() || formatNode.isNull()) {
            return;
        }
        String format = formatNode.stringValue();
        if (format == null || !VALID_FORMATS.contains(format)) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), anchor.key());
        }
    }
}
