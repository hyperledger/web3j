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
import java.lang.reflect.Method;
import java.util.List;

public class Test {

    @org.junit.jupiter.api.Test
    public void testThatGreetMethodWasGenerated() throws IOException {
        ClassProvider classProvider =
                new ClassProvider(
                        new File(
                                "/home/alexander/Documents/dev/generated/Q6/build/generated/source/web3j/main/java"));
        List<Class> compiledClasses = classProvider.getClasses();
        List<Method> filteredMethods = MethodFilter.extractValidMethods(compiledClasses.get(0));
        compiledClasses.forEach(
                c -> {
                    try {
                        new UnitClassGenerator(
                                        c,
                                        "org.com",
                                        "/home/alexander/Documents/dev/generated/Q6/src/test/java")
                                .writeClass();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        for (Method m : filteredMethods) {
            System.out.println(m.getName());
            Object[] test4 =
                    ParserUtils.generateAssertionPlaceholderValues(m, compiledClasses.get(0));
            //    System.out.println(Arrays.toString(test4));
            String test3 =
                    ParserUtils.generateAssertionJavaPoetStringTypes(m, compiledClasses.get(0));
            //  System.out.println(test3);

            System.out.println(
                    new MethodParser(m, compiledClasses.get(0)).getMethodSpec().toString());
        }
    }
}
