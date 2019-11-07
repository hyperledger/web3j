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

import static org.testcontainers.shaded.org.apache.commons.lang.ArrayUtils.addAll;
import static org.web3j.codegen.unit.gen.utills.NameUtils.returnTypeAsLiteral;
import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class Transfer extends Template {

    public Transfer(
            final Class contractName,
            final Type returnType,
            final List<Class> deployArguments,
            final List<ParameterSpec> methodParameters) {
        this.contractName = contractName;
        this.returnType = returnType;
        this.deployArguments = deployArguments;
        this.methodParameters = methodParameters;
    }

    @Override
    public MethodSpec generate() {
        return MethodSpec.methodBuilder("testTransfer")
                .addAnnotation(Test.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .addStatement("//Please use a valid address")
                .returns(TypeName.VOID)
                .addStatement("$T $L = $L.transfer(" + customParameters() + ").send()", arguments())
                .build();
    }

    @Override
    Object[] arguments() {

        return addAll(
                new Object[] {
                    returnType,
                    toCamelCase(returnTypeAsLiteral(returnType, false)),
                    contractName.getSimpleName().toLowerCase()
                },
                dynamicArguments());
    }
}
