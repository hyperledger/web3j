package org.web3j.console.project;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TemplateProviderTest {

    TemplateProvider templateProvider;
    {
        try {
            templateProvider = new TemplateProvider.Builder()
                    .loadSolidityProject("Greeter.sol")
                    .loadGradlewBatScript("gradlew.bat.template")
                    .loadGradlewScript("gradlew.template")
                    .loadMainJavaClass("Template.java")
                    .loadGradleBuild("build.gradle.template")
                    .loadGradleSettings("settings.gradle.template")
                    .loadGradlewWrapperSettings("gradlew-wrapper.properties.template")
                    .loadGradleJar("gradle-wrapper.jar")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void loadMainJavaClassTest()
    {
        Assert.assertEquals(templateProvider.getMainJavaClass(), TemplatesAsString.javaTemplate);
    }
    @Test
    public void loadGradleBuildTest()
    {
        Assert.assertEquals(templateProvider.getGradleBuild(), TemplatesAsString.gradleBuildTemplate);
    }
    @Test
    public void loadGradleSettingsTest()
    {
        Assert.assertEquals(templateProvider.getGradleSettings(), TemplatesAsString.gradleSettings);
    }
    @Test
    public void loadSolidityProjectTest()
    {
        Assert.assertEquals(templateProvider.getSolidityProject(), TemplatesAsString.solidityProject);
    }
    @Test
    public void loadGradlewScriptTest()
    {
        Assert.assertEquals(templateProvider.getGradlewScript(), TemplatesAsString.gradlewScript);
    }
    @Test
    public void loadGradlewBatScriptTest()
    {
        Assert.assertEquals(templateProvider.getGradlewBatScript(), TemplatesAsString.gradlewBatScript);
    }
    @Test
    public void loadGradleWrapperTest()
    {
        Assert.assertEquals(templateProvider.getGradlewWrapperSettings(), TemplatesAsString.gradleWrapperSettings);
    }








}
