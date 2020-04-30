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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;

/** Generator class for creating all the different numeric type variants. */
public class AbiTypesGenerator extends Generator {

    private static final String CODEGEN_WARNING = buildWarning(AbiTypesGenerator.class);

    private static final String DEFAULT = "DEFAULT";

    public static void main(final String[] args) throws Exception {
        final AbiTypesGenerator abiTypesGenerator = new AbiTypesGenerator();
        if (args.length == 1) {
            abiTypesGenerator.generate(args[0]);
        } else {
            abiTypesGenerator.generate(System.getProperty("user.dir") + "/abi/src/main/java/");
        }
    }

    private void generate(final String destinationDir) throws IOException {
        generateIntTypes(Int.class, destinationDir);
        generateIntTypes(Uint.class, destinationDir);

        // TODO: Enable once Solidity supports fixed types - see
        // https://github.com/ethereum/solidity/issues/409
        // generateFixedTypes(Fixed.class, destinationDir);
        // generateFixedTypes(Ufixed.class, destinationDir);

        generateBytesTypes(destinationDir);
        generateStaticArrayTypes(destinationDir);
    }

    private void generateIntTypes(final Class<?> superclass, final String path) throws IOException {
        final String packageName = createPackageName(superclass);
        ClassName className;

        for (int bitSize = 8; bitSize <= Type.MAX_BIT_LENGTH; bitSize += 8) {
            className = ClassName.get(packageName, superclass.getSimpleName() + bitSize);

            final MethodSpec constructorSpec =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(BigInteger.class, "value")
                            .addStatement("super($L, $N)", bitSize, "value")
                            .build();

            final MethodSpec overideConstructorSpec =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(long.class, "value")
                            .addStatement("this(BigInteger.valueOf(value))")
                            .build();

            final FieldSpec defaultFieldSpec =
                    FieldSpec.builder(
                                    className,
                                    DEFAULT,
                                    Modifier.PUBLIC,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .initializer("new $T(BigInteger.ZERO)", className)
                            .build();

            final TypeSpec intType =
                    TypeSpec.classBuilder(className.simpleName())
                            .addJavadoc(CODEGEN_WARNING)
                            .superclass(superclass)
                            .addModifiers(Modifier.PUBLIC)
                            .addField(defaultFieldSpec)
                            .addMethods(Arrays.asList(constructorSpec, overideConstructorSpec))
                            .build();

            write(packageName, intType, path);
        }
    }

    private void generateFixedTypes(final Class<?> superclass, final String path)
            throws IOException {
        final String packageName = createPackageName(superclass);
        ClassName className;

        for (int mBitSize = 8; mBitSize < Type.MAX_BIT_LENGTH; mBitSize += 8) {
            for (int nBitSize = 8; nBitSize < Type.MAX_BIT_LENGTH; nBitSize += 8) {

                if (mBitSize + nBitSize > Type.MAX_BIT_LENGTH) {
                    break;
                }

                final MethodSpec constructorSpec1 =
                        MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(BigInteger.class, "value")
                                .addStatement("super($L, $L, $N)", mBitSize, nBitSize, "value")
                                .build();

                final MethodSpec constructorSpec2 =
                        MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(int.class, "mBitSize")
                                .addParameter(int.class, "nBitSize")
                                .addParameter(BigInteger.class, "m")
                                .addParameter(BigInteger.class, "n")
                                .addStatement("super($L, $L, $N, $N)", mBitSize, nBitSize, "m", "n")
                                .build();

                className =
                        ClassName.get(
                                packageName,
                                superclass.getSimpleName() + mBitSize + "x" + nBitSize);

                final FieldSpec defaultFieldSpec =
                        FieldSpec.builder(
                                        className,
                                        DEFAULT,
                                        Modifier.PUBLIC,
                                        Modifier.STATIC,
                                        Modifier.FINAL)
                                .initializer("new $T(BigInteger.ZERO)", className)
                                .build();

                final TypeSpec fixedType =
                        TypeSpec.classBuilder(className.simpleName())
                                .addJavadoc(CODEGEN_WARNING)
                                .superclass(superclass)
                                .addModifiers(Modifier.PUBLIC)
                                .addField(defaultFieldSpec)
                                .addMethod(constructorSpec1)
                                .addMethod(constructorSpec2)
                                .build();

                write(packageName, fixedType, path);
            }
        }
    }

    private void generateBytesTypes(final String path) throws IOException {
        final String packageName = createPackageName(Bytes.class);
        ClassName className;

        for (int byteSize = 1; byteSize <= 32; byteSize++) {

            final MethodSpec constructorSpec =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(byte[].class, "value")
                            .addStatement("super($L, $N)", byteSize, "value")
                            .build();

            className = ClassName.get(packageName, Bytes.class.getSimpleName() + byteSize);

            final FieldSpec defaultFieldSpec =
                    FieldSpec.builder(
                                    className,
                                    DEFAULT,
                                    Modifier.PUBLIC,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .initializer("new $T(new byte[$L])", className, byteSize)
                            .build();

            final TypeSpec bytesType =
                    TypeSpec.classBuilder(className.simpleName())
                            .addJavadoc(CODEGEN_WARNING)
                            .superclass(Bytes.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addField(defaultFieldSpec)
                            .addMethod(constructorSpec)
                            .build();

            write(packageName, bytesType, path);
        }
    }

    private void generateStaticArrayTypes(final String path) throws IOException {
        final String packageName = createPackageName(StaticArray.class);
        ClassName className;

        for (int length = 1; length <= StaticArray.MAX_SIZE_OF_STATIC_ARRAY; length++) {

            final ParameterizedTypeName parameterizedTypeName =
                    ParameterizedTypeName.get(
                            ClassName.get(Type.class), WildcardTypeName.subtypeOf(Object.class));

            final TypeVariableName typeVariableName =
                    TypeVariableName.get("T").withBounds(parameterizedTypeName);

            final MethodSpec constructorSpec =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(
                                    ParameterizedTypeName.get(
                                            ClassName.get(Class.class), typeVariableName),
                                    "type",
                                    Modifier.FINAL)
                            .addParameter(
                                    ParameterizedTypeName.get(
                                            ClassName.get(List.class), typeVariableName),
                                    "values",
                                    Modifier.FINAL)
                            .addStatement("super(type, $L, values)", length)
                            .build();

            final MethodSpec arrayOverloadConstructorSpec =
                    MethodSpec.constructorBuilder()
                            .addAnnotation(SafeVarargs.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(
                                    ParameterizedTypeName.get(
                                            ClassName.get(Class.class), typeVariableName),
                                    "type",
                                    Modifier.FINAL)
                            .addParameter(
                                    ArrayTypeName.of(typeVariableName), "values", Modifier.FINAL)
                            .varargs()
                            .addStatement("super(type, $L, values)", length)
                            .build();

            className = ClassName.get(packageName, StaticArray.class.getSimpleName() + length);

            final TypeSpec bytesType =
                    TypeSpec.classBuilder(className.simpleName())
                            .addTypeVariable(typeVariableName)
                            .addJavadoc(CODEGEN_WARNING)
                            .superclass(
                                    ParameterizedTypeName.get(
                                            ClassName.get(StaticArray.class), typeVariableName))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethods(
                                    Arrays.asList(constructorSpec, arrayOverloadConstructorSpec))
                            .build();

            write(packageName, bytesType, path);
        }
    }

    static String createPackageName(final Class<?> cls) {
        return getPackageName(cls) + ".generated";
    }

    static String getPackageName(final Class<?> cls) {
        return cls.getPackage().getName();
    }
}
