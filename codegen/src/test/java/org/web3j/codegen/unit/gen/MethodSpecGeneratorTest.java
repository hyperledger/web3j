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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodSpecGeneratorTest {

    @Test
    public void testGenerate() {
        List<ParameterSpec> parameterSpec =
                Collections.singletonList(
                        ParameterSpec.builder(Web3j.class, "web3j", Modifier.FINAL).build());
        String javaPoetStringFormat1 = "$T $L = $S";
        Object[] replacementValues1 = new Object[] {String.class, "hello ", "Hello how are you"};
        String javaPoetStringFormat2 = "$T $L = $T.build()";
        Object[] replacementValues2 = new Object[] {Web3j.class, "web3j", Web3j.class};
        Map<String, Object[]> statementBody = new LinkedHashMap<>();
        statementBody.put(javaPoetStringFormat1, replacementValues1);
        statementBody.put(javaPoetStringFormat2, replacementValues2);
        MethodSpecGenerator methodSpecGenerator =
                new MethodSpecGenerator(
                        "unitTest", Test.class, Modifier.PUBLIC, parameterSpec, statementBody);
        MethodSpec generatedMethodSpec = methodSpecGenerator.generate();
        assertEquals(
                "@org.junit.jupiter.api.Test\n"
                        + "public void unitTest(final org.web3j.protocol.Web3j web3j) throws java.lang.Exception {\n"
                        + "  java.lang.String hello  = \"Hello how are you\";\n"
                        + "  org.web3j.protocol.Web3j web3j = org.web3j.protocol.Web3j.build();\n"
                        + "}\n",
                generatedMethodSpec.toString());
    }
}
