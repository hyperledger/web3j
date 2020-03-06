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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.*;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.codegen.generators.SolidityWrapperGeneratorConfig;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.codegen.generators.SolidityConstants.*;

public class DeployPoet extends BasicPoet {

    public DeployPoet(SolidityWrapperGeneratorConfig config) {
        super(config);
    }

    public List<MethodSpec> buildDeployMethods(
            String className, List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {
        boolean constructor = false;
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals("constructor")) {
                constructor = true;
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                Credentials.class,
                                CREDENTIALS,
                                true));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                TransactionManager.class,
                                TRANSACTION_MANAGER,
                                true));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                Credentials.class,
                                CREDENTIALS,
                                false));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                TransactionManager.class,
                                TRANSACTION_MANAGER,
                                false));
            }
        }

        // constructor will not be specified in ABI file if its empty
        if (!constructor) {
            MethodSpec.Builder credentialsMethodBuilder =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS, false, true);
            methodSpecs.add(
                    buildDeployNoParams(
                            credentialsMethodBuilder, className, CREDENTIALS, false, true));

            MethodSpec.Builder credentialsMethodBuilderNoGasProvider =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS, false, false);
            methodSpecs.add(
                    buildDeployNoParams(
                            credentialsMethodBuilderNoGasProvider,
                            className,
                            CREDENTIALS,
                            false,
                            false));

            MethodSpec.Builder transactionManagerMethodBuilder =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER, false, true);
            methodSpecs.add(
                    buildDeployNoParams(
                            transactionManagerMethodBuilder,
                            className,
                            TRANSACTION_MANAGER,
                            false,
                            true));

            MethodSpec.Builder transactionManagerMethodBuilderNoGasProvider =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER, false, false);
            methodSpecs.add(
                    buildDeployNoParams(
                            transactionManagerMethodBuilderNoGasProvider,
                            className,
                            TRANSACTION_MANAGER,
                            false,
                            false));
        }

        return methodSpecs;
    }

    private MethodSpec buildDeploy(
            String className,
            AbiDefinition functionDefinition,
            Class authType,
            String authName,
            boolean withGasProvider)
            throws ClassNotFoundException {

        boolean isPayable = functionDefinition.isPayable();

        MethodSpec.Builder methodBuilder =
                getDeployMethodSpec(className, authType, authName, isPayable, withGasProvider);
        String inputParams =
                FunctionPoet.addParameters(methodBuilder, functionDefinition.getInputs(), config);

        if (!inputParams.isEmpty()) {
            return buildDeployWithParams(
                    methodBuilder, className, inputParams, authName, isPayable, withGasProvider);
        } else {
            return buildDeployNoParams(
                    methodBuilder, className, authName, isPayable, withGasProvider);
        }
    }

    private static MethodSpec buildDeployWithParams(
            MethodSpec.Builder methodBuilder,
            String className,
            String inputParams,
            String authName,
            boolean isPayable,
            boolean withGasProvider) {

        methodBuilder.addStatement(
                "$T encodedConstructor = $T.encodeConstructor(" + "$T.<$T>asList($L)" + ")",
                String.class,
                FunctionEncoder.class,
                Arrays.class,
                Type.class,
                inputParams);
        if (isPayable && !withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, $L, encodedConstructor, $L)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY,
                    INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable && withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, encodedConstructor, $L)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY,
                    INITIAL_VALUE);
        } else if (!isPayable && !withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, encodedConstructor)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, encodedConstructor)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY);
        }

        return methodBuilder.build();
    }

    private static MethodSpec buildDeployNoParams(
            MethodSpec.Builder methodBuilder,
            String className,
            String authName,
            boolean isPayable,
            boolean withGasPRovider) {
        if (isPayable && !withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\", $L)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY,
                    INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable && withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\", $L)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY,
                    INITIAL_VALUE);
        } else if (!isPayable && !withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\")",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\")",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY);
        }

        return methodBuilder.build();
    }

    private static MethodSpec.Builder getDeployMethodSpec(
            String className,
            Class authType,
            String authName,
            boolean isPayable,
            boolean withGasProvider) {
        MethodSpec.Builder builder =
                MethodSpec.methodBuilder("deploy")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(buildRemoteCall(TypeVariableName.get(className, Type.class)))
                        .addParameter(Web3j.class, WEB3J)
                        .addParameter(authType, authName);
        if (isPayable && !withGasProvider) {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (isPayable && withGasProvider) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (!isPayable && withGasProvider) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER);
        } else {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT);
        }
    }

    private static ParameterizedTypeName buildRemoteCall(TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(RemoteCall.class), typeName);
    }
}
