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
package org.web3j.codegen.unit.gen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.web3j.EVMTest;

import static java.io.File.separator;
import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class UnitClassGenerator {
    private final Class theContract;
    private final String packageName;
    private final String projectName;

    public UnitClassGenerator(
            final Class theContract, final String packageName, final String projectName) {
        this.theContract = theContract;
        this.packageName = packageName;
        this.projectName = projectName;
    }

    public void writeClass() throws IOException {
        FieldSpec addressOne =
                FieldSpec.builder(String.class, "myAddress")
                        .addModifiers(Modifier.STATIC)
                        .initializer("$S", "0xfe3b557e8fb62b89f4916b721be55ceb828dbd73")
                        .build();
        FieldSpec addressTwo =
                FieldSpec.builder(String.class, "addressToTestAgainst")
                        .addModifiers(Modifier.STATIC)
                        .initializer("$S", "0x42699a7612a82f1d9c36148af9c77354759b210b")
                        .build();
        TypeSpec testClass =
                TypeSpec.classBuilder(theContract.getSimpleName() + "Test")
                        .addMethods(generateMethodSpecsForEachTest())
                        .addAnnotation(EVMTest.class)
                        .addField(addressOne)
                        .addField(addressTwo)
                        .addField(theContract, toCamelCase(theContract), Modifier.PRIVATE)
                        .build();

        JavaFile javaFile = JavaFile.builder(packageName, testClass).build();
        javaFile.writeTo(
                new File(
                        projectName
                                + separator
                                + "src"
                                + separator
                                + "integrationTest"
                                + separator
                                + "java"));
    }

    private List<MethodSpec> generateMethodSpecsForEachTest() {
        List<MethodSpec> listOfMethodSpecs = new ArrayList<>();
        MethodFilter.extractValidMethods(theContract)
                .forEach(
                        method -> {
                            listOfMethodSpecs.add(
                                    new MethodParser(method, theContract).getMethodSpec());
                        });
        return listOfMethodSpecs;
    }
}
