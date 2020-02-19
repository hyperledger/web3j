/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.codegen.unit.gen.kotlin;

import java.io.File;

import com.squareup.kotlinpoet.FileSpec;
import com.squareup.kotlinpoet.KModifier;
import com.squareup.kotlinpoet.PropertySpec;
import com.squareup.kotlinpoet.TypeSpec;

import org.web3j.codegen.unit.gen.MethodFilter;
import org.web3j.codegen.unit.gen.UnitClassGenerator;
import org.web3j.commons.JavaVersion;

import static org.web3j.codegen.unit.gen.utils.NameUtils.toCamelCase;

public class KotlinClassGenerator implements UnitClassGenerator {

    private final Class theContract;
    private final String packageName;
    private final String writePath;

    public KotlinClassGenerator(
            final Class theContract, final String packageName, String writePath) {
        this.theContract = theContract;
        this.packageName = packageName;
        this.writePath = writePath;
    }

    @Override
    public void writeClass() throws Exception {
        com.squareup.kotlinpoet.ClassName EVM_ANNOTATION =
                new com.squareup.kotlinpoet.ClassName("org.web3j", "EVMTest");

        com.squareup.kotlinpoet.AnnotationSpec.Builder annotationSpec =
                com.squareup.kotlinpoet.AnnotationSpec.builder(EVM_ANNOTATION);

        if (JavaVersion.getJavaVersionAsDouble() < 11) {
            com.squareup.kotlinpoet.ClassName gethContainer =
                    new com.squareup.kotlinpoet.ClassName("org.web3j", "NodeType");
            annotationSpec.addMember("%T.GETH", gethContainer);
        }
        com.squareup.kotlinpoet.ClassName TEST_INSTANCE_ANNOTATION =
                new com.squareup.kotlinpoet.ClassName("org.junit.jupiter.api", "TestInstance");

        com.squareup.kotlinpoet.AnnotationSpec.Builder testAnnotationSpec =
                com.squareup.kotlinpoet.AnnotationSpec.builder(TEST_INSTANCE_ANNOTATION);

        com.squareup.kotlinpoet.ClassName lifeCycle =
                new com.squareup.kotlinpoet.ClassName("", "TestInstance");
        testAnnotationSpec.addMember("%T.Lifecycle.PER_CLASS", lifeCycle);

        PropertySpec contractInit =
                PropertySpec.builder(toCamelCase(theContract), theContract)
                        .addModifiers(KModifier.LATEINIT, KModifier.PRIVATE)
                        .mutable(true)
                        .build();
        TypeSpec testClass =
                TypeSpec.classBuilder(theContract.getSimpleName() + "Test")
                        .addFunctions(MethodFilter.generateFunctionSpecsForEachTest(theContract))
                        .addAnnotation((annotationSpec).build())
                        .addAnnotation(testAnnotationSpec.build())
                        .addProperty(contractInit)
                        .build();
        FileSpec kotlinFile =
                FileSpec.builder(packageName, theContract.getSimpleName() + "Test")
                        .addType(testClass)
                        .build();
        kotlinFile.writeTo(new File(writePath));
    }
}
