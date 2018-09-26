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

import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;

/**
 * Generator class for creating all the different numeric type variants.
 */
public class AbiTypesGenerator extends Generator {

    private static final String CODEGEN_WARNING = buildWarning(AbiTypesGenerator.class);

    private static final String DEFAULT = "DEFAULT";

    public static void main(String[] args) throws Exception {
        AbiTypesGenerator abiTypesGenerator = new AbiTypesGenerator();
        if (args.length == 1) {
            abiTypesGenerator.generate(args[0]);
        } else {
            abiTypesGenerator.generate(System.getProperty("user.dir") + "/abi/src/main/java/");
        }
    }

    private void generate(String destinationDir) throws IOException {
        generateIntTypes(Int.class, destinationDir);
        generateIntTypes(Uint.class, destinationDir);

        // TODO: Enable once Solidity supports fixed types - see
        // https://github.com/ethereum/solidity/issues/409
        // generateFixedTypes(Fixed.class, destinationDir);
        // generateFixedTypes(Ufixed.class, destinationDir);

        generateBytesTypes(Bytes.class, destinationDir);
        generateStaticArrayTypes(StaticArray.class, destinationDir);
    }

    private <T extends Type> void generateIntTypes(
            Class<T> superclass, String path) throws IOException {
        String packageName = createPackageName(superclass);
        ClassName className;

        for (int bitSize = 8; bitSize <= Type.MAX_BIT_LENGTH; bitSize += 8) {
            className = ClassName.get(packageName, superclass.getSimpleName() + bitSize);

            MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(BigInteger.class, "value")
                    .addStatement("super($L, $N)", bitSize, "value")
                    .build();

            MethodSpec overideConstructorSpec = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(long.class, "value")
                    .addStatement("this(BigInteger.valueOf(value))")
                    .build();

            FieldSpec defaultFieldSpec = FieldSpec
                    .builder(className, DEFAULT, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T(BigInteger.ZERO)", className)
                    .build();

            TypeSpec intType = TypeSpec.classBuilder(className.simpleName())
                    .addJavadoc(CODEGEN_WARNING)
                    .superclass(superclass)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(defaultFieldSpec)
                    .addMethods(Arrays.asList(constructorSpec, overideConstructorSpec))
                    .build();

            write(packageName, intType, path);
        }
    }

    private <T extends Type> void generateFixedTypes(
            Class<T> superclass, String path) throws IOException {
        String packageName = createPackageName(superclass);
        ClassName className;

        for (int mBitSize = 8; mBitSize < Type.MAX_BIT_LENGTH; mBitSize += 8) {
            inner:
            for (int nBitSize = 8; nBitSize < Type.MAX_BIT_LENGTH; nBitSize += 8) {

                if (mBitSize + nBitSize > Type.MAX_BIT_LENGTH) {
                    break inner;
                }

                MethodSpec constructorSpec1 = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(BigInteger.class, "value")
                        .addStatement("super($L, $L, $N)", mBitSize, nBitSize, "value")
                        .build();

                MethodSpec constructorSpec2 = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(int.class, "mBitSize")
                        .addParameter(int.class, "nBitSize")
                        .addParameter(BigInteger.class, "m")
                        .addParameter(BigInteger.class, "n")
                        .addStatement("super($L, $L, $N, $N)", mBitSize, nBitSize, "m", "n")
                        .build();

                className = ClassName.get(packageName,
                                          superclass.getSimpleName() + mBitSize + "x" + nBitSize);

                FieldSpec defaultFieldSpec = FieldSpec
                        .builder(className,
                                 DEFAULT,
                                 Modifier.PUBLIC,
                                 Modifier.STATIC,
                                 Modifier.FINAL)
                        .initializer("new $T(BigInteger.ZERO)", className)
                        .build();

                TypeSpec fixedType = TypeSpec
                        .classBuilder(className.simpleName())
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

    private <T extends Type> void generateBytesTypes(
            Class<T> superclass, String path) throws IOException {
        String packageName = createPackageName(superclass);
        ClassName className;

        for (int byteSize = 1; byteSize <= 32; byteSize++) {

            MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(byte[].class, "value")
                    .addStatement("super($L, $N)", byteSize, "value")
                    .build();

            className = ClassName.get(packageName, superclass.getSimpleName() + byteSize);

            FieldSpec defaultFieldSpec = FieldSpec
                    .builder(className, DEFAULT, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T(new byte[$L])", className, byteSize)
                    .build();

            TypeSpec bytesType = TypeSpec
                    .classBuilder(className.simpleName())
                    .addJavadoc(CODEGEN_WARNING)
                    .superclass(superclass)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(defaultFieldSpec)
                    .addMethod(constructorSpec)
                    .build();

            write(packageName, bytesType, path);
        }
    }

    private <T extends Type> void generateStaticArrayTypes(
            Class<T> superclass, String path) throws IOException {
        String packageName = createPackageName(superclass);
        ClassName className;

        for (int length = 1; length <= StaticArray.MAX_SIZE_OF_STATIC_ARRAY; length++) {

            TypeVariableName typeVariableName = TypeVariableName.get("T").withBounds(Type.class);

            MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterizedTypeName.get(ClassName.get(List.class),
                            typeVariableName), "values")
                    .addStatement("super($L, $N)", length, "values")
                    .build();

            MethodSpec arrayOverloadConstructorSpec = MethodSpec.constructorBuilder()
                    .addAnnotation(SafeVarargs.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ArrayTypeName.of(typeVariableName), "values")
                    .varargs()
                    .addStatement("super($L, $N)", length, "values")
                    .build();

            className = ClassName.get(packageName, superclass.getSimpleName() + length);

            TypeSpec bytesType = TypeSpec
                    .classBuilder(className.simpleName())
                    .addTypeVariable(typeVariableName)
                    .addJavadoc(CODEGEN_WARNING)
                    .superclass(ParameterizedTypeName.get(ClassName.get(superclass),
                            typeVariableName))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethods(Arrays.asList(constructorSpec, arrayOverloadConstructorSpec))
                    .build();

            write(packageName, bytesType, path);
        }
    }

    static String createPackageName(Class<?> cls) {
        return getPackageName(cls) + ".generated";
    }

    static String getPackageName(Class<?> cls) {
        return cls.getPackage().getName();
    }
}
