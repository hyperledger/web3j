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

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

/**
 * Class that generates the unit tests classes for the contracts. The class writes to
 * src/test/java/contracts and each file is named after the contract + "Test" e.g GreeterTest
 */
public class UnitClassGenerator {
    private final Class theContract;
    private final String packageName;
    private final String writePath;

    public UnitClassGenerator(final Class theContract, final String packageName, String writePath) {
        this.theContract = theContract;
        this.packageName = packageName;
        this.writePath = writePath;
    }

    public void writeClass() throws IOException {
        ClassName EVM_ANNOTATION = ClassName.get("org.web3j", "EVMTest");

        TypeSpec testClass =
                TypeSpec.classBuilder(theContract.getSimpleName() + "Test")
                        .addMethods(generateMethodSpecsForEachTest())
                        .addAnnotation(AnnotationSpec.builder(EVM_ANNOTATION).build())
                        .addField(
                                theContract,
                                toCamelCase(theContract),
                                Modifier.PRIVATE,
                                Modifier.STATIC)
                        .build();
        JavaFile javaFile = JavaFile.builder(packageName, testClass).build();
        javaFile.writeTo(new File(writePath));
    }

    private List<MethodSpec> generateMethodSpecsForEachTest() {
        List<MethodSpec> listOfMethodSpecs = new ArrayList<>();
        MethodFilter.extractValidMethods(theContract)
                .forEach(
                        method ->
                                listOfMethodSpecs.add(
                                        new MethodParser(method, theContract).getMethodSpec()));
        return listOfMethodSpecs;
    }
}
