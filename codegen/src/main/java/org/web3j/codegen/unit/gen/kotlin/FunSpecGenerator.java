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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.ParameterSpec;
import org.junit.jupiter.api.Test;

public class FunSpecGenerator {
    private final String testMethodName;
    private final Map<String, Object[]> statementBody;
    private final List<ParameterSpec> testMethodParameters;
    private final Class<?> testMethodAnnotation;

    public FunSpecGenerator(
            final String testMethodName,
            final Class<?> testMethodAnnotation,
            final List<ParameterSpec> testMethodParameters,
            final Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = testMethodAnnotation;
        this.testMethodParameters = testMethodParameters;
    }

    public FunSpecGenerator(
            final String testMethodName, final Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodParameters = Collections.emptyList();
    }

    public FunSpec generate() {
        return FunSpec.builder(testMethodName)
                .addAnnotation(testMethodAnnotation)
                .addParameters(testMethodParameters)
                .addCode(setMethodBody())
                .build();
    }

    private CodeBlock setMethodBody() {
        final CodeBlock.Builder methodBody = CodeBlock.builder();
        statementBody.forEach(methodBody::addStatement);
        return methodBody.build();
    }
}
