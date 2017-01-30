package org.web3j.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.lang.model.element.Modifier;

/**
 * Generator class for creating all the different numeric type variants.
 */
public class AbiTypesGenerator {

    private static final String CODEGEN_WARNING = "<p>Auto generated code.<br>\n" +
            "<strong>Do not modifiy!</strong><br>\n" +
            "Please use {@link " + AbiTypesGenerator.class.getName() + "} to update.</p>\n";

    private static final String DEFAULT = "DEFAULT";

    public static void main(String[] args) throws Exception {
        AbiTypesGenerator abiTypesGenerator = new AbiTypesGenerator();
        if (args.length == 1) {
            abiTypesGenerator.generate(args[0]);
        } else {
            abiTypesGenerator.generate(System.getProperty("user.dir") + "/src/main/java/");
        }
    }

    private void generate(String destinationDir) throws IOException {
        Path path = Paths.get(destinationDir);

        generateIntTypes(Int.class, path);
        generateIntTypes(Uint.class, path);
        generateFixedTypes(Fixed.class, path);
        generateFixedTypes(Ufixed.class, path);
        generateBytesTypes(Bytes.class, path);
    }

    private <T extends Type> void generateIntTypes(
            Class<T> superclass, Path path) throws IOException {
        String packageName = createPackageName(superclass);
        ClassName className;

        for (int bitSize = 8; bitSize <= Type.MAX_BIT_LENGTH; bitSize += 8) {
            className = ClassName.get(packageName, superclass.getSimpleName() + bitSize);

            MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(BigInteger.class, "value")
                    .addStatement("super($L, $N)", bitSize, "value")
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
                    .addMethod(constructorSpec)
                    .build();

            JavaFile javaFile = JavaFile.builder(packageName, intType)
                    .skipJavaLangImports(true)
                    .build();

            javaFile.writeTo(path);
        }
    }

    private <T extends Type> void generateFixedTypes(
            Class<T> superclass, Path path) throws IOException {
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
            Class<T> superclass, Path path) throws IOException {
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

    private void write(String packageName, TypeSpec typeSpec, Path destination) throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(destination);
    }

    static String createPackageName(Class<?> cls) {
        return getPackageName(cls) + ".generated";
    }

    static String getPackageName(Class<?> cls) {
        return cls.getPackage().getName();
    }
}
