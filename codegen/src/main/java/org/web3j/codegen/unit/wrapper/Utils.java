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
package org.web3j.codegen.unit.wrapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.web3j.codegen.unit.wrapper.Constants.FUNC_NAME_PREFIX;
import static org.web3j.codegen.unit.wrapper.Constants.TYPE_FUNCTION;
import static org.web3j.utils.Lambdas.duplicatesBy;

public class Utils {

    public static String funcNameToConstant(String funcName, boolean useUpperCase) {
        if (useUpperCase) {
            return FUNC_NAME_PREFIX + funcName.toUpperCase();
        } else {
            return FUNC_NAME_PREFIX + funcName;
        }
    }

    public static Set<String> getDupeFuncNames(List<AbiDefinition> functionDefinitions) {
        return functionDefinitions.stream()
                .filter(f -> f.getName() != null && TYPE_FUNCTION.equals(f.getType()))
                .filter(duplicatesBy(f -> funcNameToConstant(f.getName(), true)))
                .map(AbiDefinition::getName)
                .collect(Collectors.toSet());
    }
}
