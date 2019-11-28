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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

public class MethodFilter {

    public static List<Method> extractValidMethods(Class contract) {
        return Arrays.stream(contract.getDeclaredMethods())
                .filter(
                        m ->
                                !m.isSynthetic()
                                        && parametersAreMatching(m)
                                        && !m.getName().toLowerCase().contains("event")
                                        && !m.getName().equals("load")
                                        && !m.getName().equals("kill"))
                .collect(Collectors.toList());
    }

    private static boolean parametersAreMatching(final Method method) {
        if (method.getName().equals("deploy") || method.getName().equals("load")) {
            return Arrays.asList(method.getParameterTypes()).contains(Web3j.class)
                    && Arrays.asList(method.getParameterTypes()).contains(TransactionManager.class)
                    && Arrays.asList(method.getParameterTypes())
                            .contains(ContractGasProvider.class);
        }
        return true;
    }
}
