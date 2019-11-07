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
package org.web3j.codegen.unit.gen.templates;

import java.lang.reflect.Type;
import java.util.List;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang.ArrayUtils;

import static org.web3j.codegen.unit.gen.utills.NameUtils.capitalizeFirstLetter;
import static org.web3j.codegen.unit.gen.utills.NameUtils.returnTypeAsLiteral;
import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class GenericTemplate extends Template {
    private final String methodName;

    public GenericTemplate(
            final Class contractName,
            final Type returnType,
            final List<Class> deployArguments,
            final List<ParameterSpec> methodParameters,
            String methodName) {
        this.contractName = contractName;
        this.returnType = returnType;
        this.deployArguments = deployArguments;
        this.methodParameters = methodParameters;
        this.methodName = methodName;
    }

    @Override
    public MethodSpec generate() {

        return MethodSpec.methodBuilder("test" + capitalizeFirstLetter(methodName))
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .returns(TypeName.VOID)
                .addStatement("// Make sure to change the placeholder arguments.")
                .addStatement(
                        "$T $L = $L." + methodName + "(" + customParameters() + ").send()",
                        arguments())
                .build();
    }

    @Override
    Object[] arguments() {
        if (returnType.getTypeName().equals(contractName.getTypeName())) {
            return ArrayUtils.addAll(
                    new Object[] {
                        returnType,
                        toCamelCase(returnTypeAsLiteral(returnType, false)),
                        toCamelCase(contractName)
                    },
                    dynamicArguments());

        } else {
            return ArrayUtils.addAll(
                    new Object[] {
                        returnType,
                        toCamelCase(returnTypeAsLiteral(returnType, true)),
                        toCamelCase(contractName)
                    },
                    dynamicArguments());
        }
    }
}
