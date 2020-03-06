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
package org.web3j.codegen.unit.wrapper.generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.*;

import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.codegen.SolidityFunctionWrapper;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.web3j.utils.Lambdas.throwAtRuntime;

class TupleMember {
    public String name;
    public String type;
    public TypeName solidityType;
    public TypeName nativeType;

    public TupleMember(String name, String type) throws ClassNotFoundException {
        this.name = name;
        this.type = type;
        this.solidityType = SolidityFunctionWrapper.buildTypeName(type);
        this.nativeType = SolidityFunctionWrapper.getNativeType(solidityType);
    }
}

public class TupleGenerator {
    private static int tupleInnerClassNumber = 1;
    private static Map<String, Integer> tupleInnerClassMap = new HashMap<>();

    public static Iterable<TypeSpec> buildTupleDefinitions(List<AbiDefinition> abi) {
        return abi.stream()
                .filter(d -> d != null && d.getInputs() != null)
                .flatMap(d -> d.getInputs().stream().filter(t -> t.getType().equals("tuple")))
                .filter(input -> input.getComponents() != null && input.getComponents().size() > 0)
                .map(input -> generateTupleClassBuilder(input.getComponents()))
                .filter(Objects::nonNull)
                .map(
                        i -> {
                            i.addModifiers(Modifier.STATIC);
                            return i.build();
                        })
                .collect(Collectors.toList());
    }

    private static TypeSpec.Builder createComponentClassBuilder(String className) {
        return TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
    }

    private static TypeSpec.Builder generateTupleClassBuilder(
            List<AbiDefinition.NamedTypeComponent> components) {
        boolean isDynamicStruct = true;

        final List<TupleMember> members =
                components.stream()
                        .map(throwAtRuntime(c -> new TupleMember(c.getName(), c.getType())))
                        .collect(Collectors.toList());

        final String functionArgsId =
                members.stream().map(i -> i.name + ":" + i.type).collect(Collectors.joining(","));

        if (tupleInnerClassMap.get(functionArgsId) != null) {
            return null;
        }

        tupleInnerClassMap.putIfAbsent(functionArgsId, tupleInnerClassNumber);

        final TypeSpec.Builder innerClassBuilder =
                createComponentClassBuilder("TupleClass" + tupleInnerClassNumber)
                        .superclass(isDynamicStruct ? DynamicStruct.class : StaticStruct.class);
        tupleInnerClassNumber++;

        for (TupleMember member : members) {
            innerClassBuilder.addField(
                    FieldSpec.builder(member.nativeType, member.name, Modifier.PUBLIC).build());
        }

        final List<ParameterSpec> constructorParamSpecs =
                members.stream()
                        .map(m -> ParameterSpec.builder(m.nativeType, m.name).build())
                        .collect(Collectors.toList());

        final String constructorSuperCall =
                String.format(
                        "super(%s)",
                        members.stream()
                                .map(
                                        i ->
                                                String.format(
                                                        "new %s(%s)",
                                                        i.solidityType.toString(), i.name))
                                .collect(Collectors.joining(", ")));

        MethodSpec.Builder builder =
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameters(constructorParamSpecs)
                        .addStatement(constructorSuperCall);
        constructorParamSpecs.forEach(s -> builder.addStatement("this.$N = $N", s.name, s.name));

        innerClassBuilder.addMethod(builder.build());

        return innerClassBuilder;
    }

    public static String buildClassNameForTupleComponents(
            List<AbiDefinition.NamedTypeComponent> components) {
        final String functionArgsId =
                IntStream.range(0, components.size())
                        .mapToObj(Objects.requireNonNull(components)::get)
                        .map(comp -> comp.getName() + ":" + comp.getType())
                        .collect(Collectors.joining(","));
        final Integer tupleClassNum = tupleInnerClassMap.get(functionArgsId);
        return "TupleClass" + tupleClassNum;
    }
}
