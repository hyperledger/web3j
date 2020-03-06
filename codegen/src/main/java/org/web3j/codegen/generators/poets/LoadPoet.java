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

import java.math.BigInteger;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;

import org.web3j.abi.datatypes.Type;
import org.web3j.codegen.generators.SolidityWrapperGeneratorConfig;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.generators.SolidityConstants.*;
import static org.web3j.codegen.generators.SolidityConstants.GAS_LIMIT;

public class LoadPoet extends BasicPoet {

    public LoadPoet(SolidityWrapperGeneratorConfig config) {
        super(config);
    }

    public static MethodSpec buildLoad(
            String className, Class authType, String authName, boolean withGasProvider) {
        MethodSpec.Builder toReturn =
                MethodSpec.methodBuilder("load")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeVariableName.get(className, Type.class))
                        .addParameter(String.class, CONTRACT_ADDRESS)
                        .addParameter(Web3j.class, WEB3J)
                        .addParameter(authType, authName);

        if (withGasProvider) {
            toReturn.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addStatement(
                            "return new $L($L, $L, $L, $L)",
                            className,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            CONTRACT_GAS_PROVIDER);
        } else {
            toReturn.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addStatement(
                            "return new $L($L, $L, $L, $L, $L)",
                            className,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            GAS_PRICE,
                            GAS_LIMIT)
                    .addAnnotation(Deprecated.class);
        }

        return toReturn.build();
    }
}
