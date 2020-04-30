/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.codegen;

import java.io.File;
import java.io.IOException;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

/** Common code generator methods. */
class Generator {
    void write(final String packageName, final TypeSpec typeSpec, final String destinationDir)
            throws IOException {
        final JavaFile javaFile =
                JavaFile.builder(packageName, typeSpec)
                        .indent("    ")
                        .skipJavaLangImports(true)
                        .build();

        javaFile.writeTo(new File(destinationDir));
    }

    static String buildWarning(final Class<?> cls) {
        return "Auto generated code.\n"
                + "<p><strong>Do not modify!</strong>\n"
                + "<p>Please use "
                + cls.getName()
                + " in the \n"
                + "<a href=\"https://github.com/web3j/web3j/tree/master/codegen\">"
                + "codegen module</a> to update.\n";
    }
}
