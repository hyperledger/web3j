/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.console.project;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.junit.Assert.assertFalse;

public class TemplateProviderTest extends TempFileProvider {
    private TemplateProvider templateProvider;

    @Before
    public void init() throws IOException {
        templateProvider =
                new TemplateProvider.Builder()
                        .loadGradlewBatScript("gradlew.bat.template")
                        .loadGradlewScript("gradlew.template")
                        .loadMainJavaClass("Template.java")
                        .loadGradleBuild("build.gradle.template")
                        .loadGradleSettings("settings.gradle.template")
                        .loadGradlewWrapperSettings("gradlew-wrapper.properties.template")
                        .loadGradleJar("gradle-wrapper.jar")
                        .withPackageNameReplacement(s -> s.replaceAll("<package_name>", "test"))
                        .withProjectNameReplacement(s -> s.replaceAll("<project_name>", "test"))
                        .build();
    }

    @Test
    public void loadMainJavaClassTest() {
        assertFalse(templateProvider.getMainJavaClass().isEmpty());
    }

    @Test
    public void loadGradleBuildTest() {
        assertFalse(templateProvider.getGradleBuild().isEmpty());
    }

    @Test
    public void loadGradleSettingsTest() {
        assertFalse(templateProvider.getGradleSettings().isEmpty());
    }

    @Test
    public void loadGradlewScriptTest() {
        assertFalse(templateProvider.getGradlewScript().isEmpty());
    }

    @Test
    public void loadGradlewBatScriptTest() {
        assertFalse(templateProvider.getGradlewBatScript().isEmpty());
    }

    @Test
    public void loadGradleWrapperTest() {
        assertFalse(templateProvider.getGradlewWrapperSettings().isEmpty());
    }
}
