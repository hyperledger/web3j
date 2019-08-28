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

import org.junit.Assert;
import org.junit.Test;

public class TemplateProviderTest {

    TemplateProvider templateProvider;

    {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadMainJavaClassTest() {

        Assert.assertEquals(templateProvider.getMainJavaClass(), TemplatesAsString.javaTemplate);
    }

    @Test
    public void loadGradleBuildTest() {
        Assert.assertEquals(
                templateProvider.getGradleBuild(), TemplatesAsString.gradleBuildTemplate);
    }

    @Test
    public void loadGradleSettingsTest() {
        Assert.assertEquals(templateProvider.getGradleSettings(), TemplatesAsString.gradleSettings);
    }

    @Test
    public void loadGradlewScriptTest() {
        Assert.assertEquals(templateProvider.getGradlewScript(), TemplatesAsString.gradlewScript);
    }

    @Test
    public void loadGradlewBatScriptTest() {
        Assert.assertEquals(
                templateProvider.getGradlewBatScript(), TemplatesAsString.gradlewBatScript);
    }

    @Test
    public void loadGradleWrapperTest() {
        Assert.assertEquals(
                templateProvider.getGradlewWrapperSettings(),
                TemplatesAsString.gradleWrapperSettings);
    }
}
