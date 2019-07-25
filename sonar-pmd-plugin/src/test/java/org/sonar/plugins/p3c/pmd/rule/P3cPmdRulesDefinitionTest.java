/*
 * SonarQube PMD Plugin
 * Copyright (C) 2012-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
package org.sonar.plugins.p3c.pmd.rule;

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.p3c.pmd.PmdConstants;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class P3cPmdRulesDefinitionTest {

    @Test
    void should_init_p3c_pmd_rules_definition_successfully() {
        P3cPmdRulesDefinition definition = new P3cPmdRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository(PmdConstants.REPOSITORY_KEY);

        assertThat(repository.name()).isEqualTo(PmdConstants.REPOSITORY_NAME);
        assertThat(repository.language()).isEqualTo(PmdConstants.LANGUAGE_KEY);

        List<RulesDefinition.Rule> rules = repository.rules();
        assertThat(rules).hasSize(56);

        for (RulesDefinition.Rule rule : rules) {
            assertThat(rule.key()).isNotNull();
            assertThat(rule.internalKey()).isNotNull();
            assertThat(rule.name()).isNotNull();
            assertThat(rule.htmlDescription()).isNotNull();
            assertThat(rule.severity()).isNotNull();

            for (RulesDefinition.Param param : rule.params()) {
                assertThat(param.name()).isNotNull();
                assertThat(param.description())
                        .overridingErrorMessage("Description is not set for parameter '" + param.name() + "' of rule '" + rule.key())
                        .isNotNull();
            }
        }
    }

    @Test
    void should_exclude_junit_rules() {
        P3cPmdRulesDefinition definition = new P3cPmdRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository(PmdConstants.REPOSITORY_KEY);

        for (RulesDefinition.Rule rule : repository.rules()) {
            assertThat(rule.key()).doesNotContain("JUnitStaticSuite");
        }
    }

    @Test
    void should_use_text_parameter_for_xpath_rule() {
        P3cPmdRulesDefinition definition = new P3cPmdRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository(PmdConstants.REPOSITORY_KEY);

        RulesDefinition.Rule commentsMustBeJavadocFormatRule = Iterables.find(repository.rules(), rule -> rule.key().equals("CommentsMustBeJavadocFormatRule"));
        assertThat(commentsMustBeJavadocFormatRule.internalKey()).isEqualTo("rulesets/java/ali-comment.xml/CommentsMustBeJavadocFormatRule");
    }
}
