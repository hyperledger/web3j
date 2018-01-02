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

/**
 * A class for generating arbitrary sized tuples.
 */
public class TupleGenerator extends Generator {

    static final int LIMIT = 20;
    static final String PACKAGE_NAME = "org.web3j.tuples.generated";
    static final String CLASS_NAME = "Tuple";

    private static final String SIZE = "SIZE";
    private static final String RESULT = "result";
    private static final String VALUE = "value";

    public static void main(String[] args) throws IOException {
        TupleGenerator tupleGenerator = new TupleGenerator();
        if (args.length == 1) {
            tupleGenerator.generate(args[0]);
        } else {
            tupleGenerator.generate(System.getProperty("user.dir") + "/tuples/src/main/java/");
        }
    }

    private void generate(String destinationDir) throws IOException {
        for (int i = 1; i <= LIMIT; i++) {
            TypeSpec typeSpec = createTuple(i);

            write(PACKAGE_NAME, typeSpec, destinationDir);
        }
    }

    private TypeSpec createTuple(int size) {
        String className = CLASS_NAME + size;
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addSuperinterface(Tuple.class)
                .addField(FieldSpec.builder(int.class, SIZE)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$L", size)
                        .build());

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        List<MethodSpec> methodSpecs = new ArrayList<>(size);

        for (int i = 1; i <= size; i++) {
            String value = VALUE + i;
            TypeVariableName typeVariableName = TypeVariableName.get("T" + i);

            typeSpecBuilder.addTypeVariable(typeVariableName)
                    .addField(typeVariableName, value, Modifier.PRIVATE, Modifier.FINAL);

            constructorBuilder.addParameter(typeVariableName, value)
                    .addStatement("this.$N = $N", value, value);

            MethodSpec getterSpec = MethodSpec.methodBuilder(
                    "get" + Strings.capitaliseFirstLetter(value))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeVariableName)
                    .addStatement("return $N", value)
                    .build();
            methodSpecs.add(getterSpec);
        }

        MethodSpec constructorSpec = constructorBuilder.build();
        MethodSpec sizeSpec = generateSizeSpec();
        MethodSpec equalsSpec = generateEqualsSpec(className, size);
        MethodSpec hashCodeSpec = generateHashCodeSpec(size);
        MethodSpec toStringSpec = generateToStringSpec(size);

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
        return MethodSpec.methodBuilder(
                "getSize")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $L", SIZE)
                .build();
    }

    private MethodSpec generateEqualsSpec(String className, int size) {
        MethodSpec.Builder equalsSpecBuilder = MethodSpec.methodBuilder("equals")
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
        String wildcardClassName = className + "<" + typeParams + ">";

        String name = "tuple" + size;
        equalsSpecBuilder
                .addStatement("$L $L = ($L) o", wildcardClassName, name, wildcardClassName);

        for (int i = 1; i < size; i++) {
            String value = VALUE + i;

            equalsSpecBuilder.beginControlFlow(
                    "if ($L != null ? !$L.equals($L.$L) : $L.$L != null)",
                    value, value, name, value, name, value)
                    .addStatement("return false")
                    .endControlFlow();
        }

        String lastValue = VALUE + size;
        equalsSpecBuilder
                .addStatement("return $L != null ? $L.equals($L.$L) : $L.$L == null",
                        lastValue, lastValue, name, lastValue, name, lastValue);

        return equalsSpecBuilder.build();
    }

    private MethodSpec generateHashCodeSpec(int size) {
        MethodSpec.Builder hashCodeSpec = MethodSpec.methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("int $L = $L.hashCode()", RESULT, VALUE + 1);

        for (int i = 2; i <= size; i++) {
            String value = "value" + i;
            hashCodeSpec.addStatement(
                    "$L = 31 * $L + ($L != null ? $L.hashCode() : 0)",
                    RESULT, RESULT, value, value);
        }

        hashCodeSpec.addStatement("return $L", RESULT);

        return hashCodeSpec.build();
    }

    private MethodSpec generateToStringSpec(int size) {
        String toString = "return \"" + CLASS_NAME + size + "{\" +\n";
        String firstValue = VALUE + 1;
        toString += "\"" + firstValue + "=\"" + " + " + firstValue + " +\n";

        for (int i = 2; i <= size; i++) {
            String value = VALUE + i;
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
