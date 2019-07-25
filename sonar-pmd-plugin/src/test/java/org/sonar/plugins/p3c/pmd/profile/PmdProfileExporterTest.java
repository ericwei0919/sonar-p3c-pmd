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
package org.sonar.plugins.p3c.pmd.profile;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.p3c.pmd.PmdConstants;
import org.sonar.plugins.p3c.pmd.PmdTestUtils;
import org.sonar.plugins.p3c.pmd.rule.P3cPmdRulesDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PmdProfileExporterTest {

    private static final CharMatcher EOLS = CharMatcher.anyOf("\n\r");

    private final PmdProfileExporter exporter = new PmdProfileExporter();

    private static RulesProfile importProfile(String configuration) {
        P3cPmdRulesDefinition definition = new P3cPmdRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository(PmdConstants.REPOSITORY_KEY);
        RuleFinder ruleFinder = createRuleFinder(repository.rules());
        PmdProfileImporter importer = new PmdProfileImporter(ruleFinder);

        return importer.importProfile(new StringReader(configuration), ValidationMessages.create());
    }

    private static RuleFinder createRuleFinder(final List<RulesDefinition.Rule> rules) {
        RuleFinder ruleFinder = mock(RuleFinder.class);
        final List<Rule> convertedRules = convert(rules);

        when(ruleFinder.find(any(RuleQuery.class))).then((Answer<Rule>) invocation -> {
            RuleQuery query = (RuleQuery) invocation.getArguments()[0];
            for (Rule rule : convertedRules) {
                if (query.getConfigKey().equals(rule.getConfigKey())) {
                    return rule;
                }
            }
            return null;
        });
        return ruleFinder;
    }

    private static List<Rule> convert(List<RulesDefinition.Rule> rules) {
        List<Rule> results = new ArrayList<>(rules.size());
        for (RulesDefinition.Rule rule : rules) {
            Rule newRule = Rule.create(rule.repository().key(), rule.key(), rule.name())
                    .setDescription(rule.htmlDescription())
                    .setRepositoryKey(rule.repository().key())
                    .setConfigKey(rule.internalKey());
            if (!rule.params().isEmpty()) {
                for (Param param : rule.params()) {
                    newRule.createParameter(param.name()).setDefaultValue(param.defaultValue());
                }
            }
            results.add(newRule);
        }
        return results;
    }

    private static Condition<String> equalsIgnoreEOL(String text) {
        final String strippedText = EOLS.removeFrom(text);

        return new Condition<String>() {
            @Override
            public boolean matches(String value) {
                return EOLS.removeFrom(value).equals(strippedText);
            }
        }.as("equal to " + strippedText);
    }

    @Test
    void should_export_pmd_profile_on_writer() {
        String importedXml = PmdTestUtils.getResourceContent("/org/sonar/plugins/p3c/pmd/export_simple.xml");

        StringWriter stringWriter = new StringWriter();
        exporter.exportProfile(importProfile(importedXml), stringWriter);

        assertThat(stringWriter.toString()).satisfies(equalsIgnoreEOL(importedXml));
    }

    @Test
    void should_export_pmd_profile_on_writer_exception() throws IOException {

        // given
        final String importedXml = PmdTestUtils.getResourceContent("/org/sonar/plugins/p3c/pmd/export_simple.xml");
        final Writer writer = mock(Writer.class);
        doThrow(new IOException("test exception")).when(writer).write(anyString());

        // when
        final Throwable thrown = catchThrowable(() -> exporter.exportProfile(importProfile(importedXml), writer));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("An exception occurred while generating the PMD configuration file from profile: null");
    }

    @Test
    void should_skip_all_empty_params() {
        String importedXml = PmdTestUtils.getResourceContent("/org/sonar/plugins/p3c/pmd/export_rule_with_all_params_empty.xml");

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ruleset name=\"p3c-pmd\">\n" +
                "  <description>Sonar Profile: p3c-pmd</description>\n" +
                "  <rule ref=\"rulesets/java/ali-concurrent.xml/ThreadPoolCreationRule\">\n" +
                "    <priority>2</priority>\n" +
                "  </rule>\n" +
                "</ruleset>";

        final StringWriter writer = new StringWriter();
        exporter.exportProfile(importProfile(importedXml), writer);
        assertThat(writer.toString()).satisfies(equalsIgnoreEOL(expected));
    }

    @Test
    void should_export_empty_configuration_as_xml() {

        final StringWriter writer = new StringWriter();

        exporter.exportProfile(RulesProfile.create(), writer);

        assertThat(writer.toString()).satisfies(equalsIgnoreEOL("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<ruleset name=\"p3c-pmd\">  <description>Sonar Profile: p3c-pmd</description></ruleset>"));
    }

    @Test
    void should_export_xPath_rule() {
        Rule rule = Rule.create(PmdConstants.REPOSITORY_KEY, "MyOwnRule", "This is my own xpath rule.")
                .setConfigKey(PmdConstants.XPATH_CLASS)
                .setRepositoryKey(PmdConstants.REPOSITORY_KEY);
        rule.createParameter(PmdConstants.XPATH_EXPRESSION_PARAM);
        rule.createParameter(PmdConstants.XPATH_MESSAGE_PARAM);

        RulesProfile profile = RulesProfile.create();
        ActiveRule xpath = profile.activateRule(rule, null);
        xpath.setParameter(PmdConstants.XPATH_EXPRESSION_PARAM, "//FieldDeclaration");
        xpath.setParameter(PmdConstants.XPATH_MESSAGE_PARAM, "This is bad");

        final StringWriter writer = new StringWriter();
        exporter.exportProfile(profile, writer);


        assertThat(writer.toString()).satisfies(equalsIgnoreEOL(PmdTestUtils.getResourceContent("/org/sonar/plugins/p3c/pmd/export_xpath_rules.xml")));
    }
}
