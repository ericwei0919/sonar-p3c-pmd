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
package org.sonar.plugins.p3c.pmd;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.plugins.p3c.pmd.profile.PmdProfileExporter;
import org.sonar.plugins.p3c.pmd.profile.PmdProfileImporter;
import org.sonar.plugins.p3c.pmd.rule.P3cPmdRulesDefinition;

/**
 * The {@link PmdPlugin} is the main entry-point of Sonar-PMD.
 */
public class PmdPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
                PropertyDefinition.builder(PmdConfiguration.PROPERTY_GENERATE_XML)
                        .defaultValue("false")
                        .name("Generate XML Report")
                        .hidden()
                        .build(),

                PmdSensor.class,
                PmdConfiguration.class,
                PmdExecutor.class,
                P3cPmdRulesDefinition.class,
                PmdProfileExporter.class,
                PmdProfileImporter.class,
                PmdViolationRecorder.class
        );
    }
}
