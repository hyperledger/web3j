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
package org.web3j.codegen.generators.poets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.web3j.tx.Contract.FUNC_DEPLOY;
import static org.web3j.utils.Lambdas.distinctBy;

public class FunctionNamePoet {

    public static Iterable<FieldSpec> buildFunctionNameConstants(
            List<AbiDefinition> abiDefinitions) {
        Set<String> dupeNames = PoetUtils.getDupeFuncNames(abiDefinitions);
        if (!dupeNames.isEmpty()) {
            System.out.println(
                    "\nWarning: Duplicate field(s) found: "
                            + dupeNames
                            + ". Please don't use names which will be the same in uppercase.");
        }

        return abiDefinitions.stream()
                .filter(distinctBy(AbiDefinition::getName))
                .filter(i -> !i.getName().equals(FUNC_DEPLOY))
                .map(
                        d -> {
                            String funcName = d.getName();
                            boolean useUpperCase =
                                    !dupeNames.contains(
                                            PoetUtils.funcNameToConstant(funcName, true));
                            return FieldSpec.builder(
                                            String.class,
                                            PoetUtils.funcNameToConstant(funcName, useUpperCase),
                                            Modifier.PUBLIC,
                                            Modifier.STATIC,
                                            Modifier.FINAL)
                                    .initializer("$S", funcName)
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
