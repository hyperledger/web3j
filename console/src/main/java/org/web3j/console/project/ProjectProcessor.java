package org.web3j.console.project;

public class ProjectProcessor {
    private final ProjectStructure projectStructure;
    private final TemplateProvider templateProvider;
    private String javaClass;
    private String gradleBuild;
    private String gradleSettings;

    public String getJavaClass() {
        return javaClass;
    }

    public String getGradleBuild() {
        return gradleBuild;
    }

    public String getGradleSettings() {
        return gradleSettings;
    }

    public ProjectProcessor(ProjectStructure projectStructure, TemplateProvider templateProvider) {
        this.projectStructure = projectStructure;
        this.templateProvider = templateProvider;
    }

    private void mainJavaClass() {

        this.javaClass = customizeFile(templateProvider.getMainJavaClass(), "<package_name>", projectStructure.getPackageName());

        this.javaClass = customizeFile(javaClass, "<class_name>", projectStructure.getProjectName());


    }

    private void gradleBuild() {
        this.gradleBuild = customizeFile(templateProvider.getGradleBuild(), "<package_name>", projectStructure.getPackageName());

    }

    private void gradleSettings() {
        this.gradleSettings = customizeFile(templateProvider.getGradleSettings(), "<project_name>", projectStructure.getProjectName());

    }


    private String customizeFile(String template, String regex, String replacement) {
        return template.replaceAll(regex, replacement);
    }
    public void process( )
    {
        mainJavaClass();
        gradleBuild();
        gradleSettings();


    }

}
