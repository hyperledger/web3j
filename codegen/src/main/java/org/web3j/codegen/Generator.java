package org.web3j.codegen;

import java.io.File;
import java.io.IOException;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

/**
 * Common code generator methods.
 */
class Generator {
    void write(String packageName, TypeSpec typeSpec, String destinationDir) throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("    ")
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(new File(destinationDir));
    }

    static String buildWarning(Class cls) {
        return "Auto generated code.\n"
                + "<p><strong>Do not modify!</strong>\n"
                + "<p>Please use " + cls.getName() + " in the \n"
                + "<a href=\"https://github.com/web3j/web3j/tree/master/codegen\">"
                + "codegen module</a> to update.\n";
    }
}
