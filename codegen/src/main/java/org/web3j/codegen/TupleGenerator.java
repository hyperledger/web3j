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
package org.web3j.codegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import org.web3j.tuples.Tuple;
import org.web3j.utils.Strings;

/** A class for generating arbitrary sized tuples. */
public class TupleGenerator extends Generator {

    static final int LIMIT = 20;
    static final String PACKAGE_NAME = "org.web3j.tuples.generated";
    static final String CLASS_NAME = "Tuple";

    private static final String SIZE = "SIZE";
    private static final String RESULT = "result";
    private static final String VALUE = "value";

    public static void main(final String[] args) throws IOException {
        final TupleGenerator tupleGenerator = new TupleGenerator();
        if (args.length == 1) {
            tupleGenerator.generate(args[0]);
        } else {
            tupleGenerator.generate(System.getProperty("user.dir") + "/tuples/src/main/java/");
        }
    }

    private void generate(final String destinationDir) throws IOException {
        for (int i = 1; i <= LIMIT; i++) {
            final TypeSpec typeSpec = createTuple(i);

            write(PACKAGE_NAME, typeSpec, destinationDir);
        }
    }

    private TypeSpec createTuple(final int size) {
        final String javadoc =
                "@deprecated use 'component$L' method instead \n @return returns a value";
        final String className = CLASS_NAME + size;
        final TypeSpec.Builder typeSpecBuilder =
                TypeSpec.classBuilder(className)
                        .addSuperinterface(Tuple.class)
                        .addField(
                                FieldSpec.builder(int.class, SIZE)
                                        .addModifiers(
                                                Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                        .initializer("$L", size)
                                        .build());

        final MethodSpec.Builder constructorBuilder =
                MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        final List<MethodSpec> methodSpecs = new ArrayList<>(size);

        for (int i = 1; i <= size; i++) {
            final String value = VALUE + i;
            final TypeVariableName typeVariableName = TypeVariableName.get("T" + i);

            typeSpecBuilder
                    .addTypeVariable(typeVariableName)
                    .addField(typeVariableName, value, Modifier.PRIVATE, Modifier.FINAL);

            constructorBuilder
                    .addParameter(typeVariableName, value)
                    .addStatement("this.$N = $N", value, value);

            final MethodSpec getterSpec =
                    MethodSpec.methodBuilder("get" + Strings.capitaliseFirstLetter(value))
                            .addAnnotation(Deprecated.class)
                            .addJavadoc(javadoc, i)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(typeVariableName)
                            .addStatement("return $N", value)
                            .build();
            methodSpecs.add(getterSpec);

            final MethodSpec getterSpec2 =
                    MethodSpec.methodBuilder("component" + i)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(typeVariableName)
                            .addStatement("return $N", value)
                            .build();
            methodSpecs.add(getterSpec2);
        }

        final MethodSpec constructorSpec = constructorBuilder.build();
        final MethodSpec sizeSpec = generateSizeSpec();
        final MethodSpec equalsSpec = generateEqualsSpec(className, size);
        final MethodSpec hashCodeSpec = generateHashCodeSpec(size);
        final MethodSpec toStringSpec = generateToStringSpec(size);

        return typeSpecBuilder
                .addJavadoc(buildWarning(TupleGenerator.class))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructorSpec)
                .addMethods(methodSpecs)
                .addMethod(sizeSpec)
                .addMethod(equalsSpec)
                .addMethod(hashCodeSpec)
                .addMethod(toStringSpec)
                .build();
    }

    private MethodSpec generateSizeSpec() {
        return MethodSpec.methodBuilder("getSize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $L", SIZE)
                .build();
    }

    private MethodSpec generateEqualsSpec(final String className, final int size) {
        final MethodSpec.Builder equalsSpecBuilder =
                MethodSpec.methodBuilder("equals")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Object.class, "o")
                        .returns(boolean.class)
                        .beginControlFlow("if (this == o)")
                        .addStatement("return true")
                        .endControlFlow()
                        .beginControlFlow("if (o == null || getClass() != o.getClass())")
                        .addStatement("return false")
                        .endControlFlow();

        String typeParams = Strings.repeat('?', size).replaceAll("\\?", "?, ");
        typeParams = typeParams.substring(0, typeParams.length() - 2);
        final String wildcardClassName = className + "<" + typeParams + ">";

        final String name = "tuple" + size;
        equalsSpecBuilder.addStatement(
                "$L $L = ($L) o", wildcardClassName, name, wildcardClassName);

        for (int i = 1; i < size; i++) {
            final String value = VALUE + i;

            equalsSpecBuilder
                    .beginControlFlow(
                            "if ($L != null ? !$L.equals($L.$L) : $L.$L != null)",
                            value,
                            value,
                            name,
                            value,
                            name,
                            value)
                    .addStatement("return false")
                    .endControlFlow();
        }

        final String lastValue = VALUE + size;
        equalsSpecBuilder.addStatement(
                "return $L != null ? $L.equals($L.$L) : $L.$L == null",
                lastValue,
                lastValue,
                name,
                lastValue,
                name,
                lastValue);

        return equalsSpecBuilder.build();
    }

    private MethodSpec generateHashCodeSpec(final int size) {
        final MethodSpec.Builder hashCodeSpec =
                MethodSpec.methodBuilder("hashCode")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(int.class)
                        .addStatement("int $L = $L.hashCode()", RESULT, VALUE + 1);

        for (int i = 2; i <= size; i++) {
            final String value = "value" + i;
            hashCodeSpec.addStatement(
                    "$L = 31 * $L + ($L != null ? $L.hashCode() : 0)",
                    RESULT,
                    RESULT,
                    value,
                    value);
        }

        hashCodeSpec.addStatement("return $L", RESULT);

        return hashCodeSpec.build();
    }

    private MethodSpec generateToStringSpec(final int size) {
        String toString = "return \"" + CLASS_NAME + size + "{\" +\n";
        final String firstValue = VALUE + 1;
        toString += "\"" + firstValue + "=\"" + " + " + firstValue + " +\n";

        for (int i = 2; i <= size; i++) {
            final String value = VALUE + i;
            toString += "\", " + value + "=\"" + " + " + value + " +\n";
        }

        toString += "\"}\"";

        return MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement(toString)
                .build();
    }
}
