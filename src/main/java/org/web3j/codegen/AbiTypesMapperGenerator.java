package org.web3j.codegen;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.WildcardType;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.squareup.javapoet.*;

import org.web3j.abi.datatypes.*;

/**
 * Generator class for creating all the different numeric type variants.
 */
public class AbiTypesMapperGenerator {

    private static final String CODEGEN_WARNING = "<p>Auto generated code.<br>\n" +
            "<strong>Do not modifiy!</strong><br>\n" +
            "Please use Generator located in project X to update.</p>\n";

    public static void main(String[] args) throws Exception {
        new AbiTypesMapperGenerator()
            .generate();
    }

    private void generate() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/java/");

        String packageName = "org.web3j.abi.datatypes.generated";

        MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getType")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "type")
                .returns(
                        ParameterizedTypeName.get(ClassName.get(Class.class),
                                WildcardTypeName.subtypeOf(Object.class))
                )
                .beginControlFlow("switch (type)");

        builder = addTypes(builder, packageName);
        builder = builder.addStatement("default:\nthrow new $T($S)",
                UnsupportedOperationException.class,
                "Unsupported type encountered");
        builder.endControlFlow();

        MethodSpec methodSpec = builder.build();

        TypeSpec typeSpec = TypeSpec
                .classBuilder("AbiTypes")
                .addJavadoc(CODEGEN_WARNING)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructorSpec)
                .addMethod(methodSpec)
                .build();

       write(packageName, typeSpec, path);
    }

    private void write(String packageName, TypeSpec typeSpec, Path destination) throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(destination);
    }

    private MethodSpec.Builder addTypes(MethodSpec.Builder builder, String packageName) {


        for (int bitSize = 8; bitSize <= Type.MAX_BIT_LENGTH; bitSize += 8) {

            builder = addStatement(builder, packageName,
                    Uint.TYPE_NAME + bitSize, Uint.class.getSimpleName() + bitSize);
            builder = addStatement(builder, packageName,
                    Int.TYPE_NAME + bitSize, Int.class.getSimpleName() + bitSize);
        }

        for (int mBitSize = 8, nBitSize = Type.MAX_BIT_LENGTH - 8;
             mBitSize < Type.MAX_BIT_LENGTH && nBitSize > 0;
             mBitSize += 8, nBitSize -= 8) {
            String suffix = mBitSize + "x" + nBitSize;
            builder = addStatement(
                    builder, packageName, Ufixed.TYPE_NAME + suffix,
                    Ufixed.class.getSimpleName() + suffix);
            builder = addStatement(
                    builder, packageName, Fixed.TYPE_NAME + suffix,
                    Fixed.class.getSimpleName() + suffix);
        }

        for (int byteSize = 1; byteSize <= 32; byteSize++) {
            builder = addStatement(builder, packageName,
                    Bytes.TYPE_NAME + byteSize, Bytes.class.getSimpleName() + byteSize);
        }

        return builder;
    }

    private MethodSpec.Builder addStatement(MethodSpec.Builder builder, String packageName,
                                            String typeName, String className) {
        return builder.addStatement(
                "case \"$L\":\nreturn $T.class", typeName, ClassName.get(packageName, className));
    }

}
