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
import javax.lang.model.element.Modifier;

import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.ParameterSpec;
import org.junit.jupiter.api.Test;

public class FunSpecGenerator {
    private final String testMethodName;
    private final Map<String, Object[]> statementBody;
    private final List<ParameterSpec> testMethodParameters;
    private final Class testMethodAnnotation;
    private final Modifier testMethodModifier;

    public FunSpecGenerator(
            String testMethodName,
            Class testMethodAnnotation,
            List<ParameterSpec> testMethodParameters,
            Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = testMethodAnnotation;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = testMethodParameters;
    }

    public FunSpecGenerator(String testMethodName, Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = Collections.emptyList();
    }

    public FunSpecGenerator(
            String testMethodName,
            Map<String, Object[]> statementBody,
            List<ParameterSpec> testMethodParameters) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = testMethodParameters;
    }

    public FunSpec generate() {
        return FunSpec.builder(testMethodName)
                .addAnnotation(testMethodAnnotation)
                .addParameters(testMethodParameters)
                .addCode(setMethodBody())
                .build();
    }

    private CodeBlock setMethodBody() {
        CodeBlock.Builder methodBody = CodeBlock.builder();
        statementBody.forEach(methodBody::addStatement);
        return methodBody.build();
    }
}
