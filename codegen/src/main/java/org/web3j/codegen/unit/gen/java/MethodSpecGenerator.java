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
package org.web3j.codegen.unit.gen.java;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Test;

public class MethodSpecGenerator {
    private final String testMethodName;
    private final Map<String, Object[]> statementBody;
    private final List<ParameterSpec> testMethodParameters;
    private final Class<?> testMethodAnnotation;
    private final Modifier testMethodModifier;

    public MethodSpecGenerator(
            final String testMethodName,
            final Class<?> testMethodAnnotation,
            final Modifier testMethodModifier,
            final List<ParameterSpec> testMethodParameters,
            final Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = testMethodAnnotation;
        this.testMethodModifier = testMethodModifier;
        this.testMethodParameters = testMethodParameters;
    }

    public MethodSpecGenerator(
            final String testMethodName, final Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = Collections.emptyList();
    }

    public MethodSpecGenerator(
            final String testMethodName,
            final Map<String, Object[]> statementBody,
            final List<ParameterSpec> testMethodParameters) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = testMethodParameters;
    }

    public MethodSpec generate() {
        return MethodSpec.methodBuilder(testMethodName)
                .addAnnotation(testMethodAnnotation)
                .addModifiers(testMethodModifier)
                .addParameters(testMethodParameters)
                .addException(Exception.class)
                .returns(TypeName.VOID)
                .addCode(setMethodBody())
                .build();
    }

    private CodeBlock setMethodBody() {
        final CodeBlock.Builder methodBody = CodeBlock.builder();
        statementBody.forEach(methodBody::addStatement);
        return methodBody.build();
    }
}
