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
package org.web3j.codegen.generators;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.primitive.Byte;
import org.web3j.abi.datatypes.primitive.Char;
import org.web3j.abi.datatypes.primitive.Double;
import org.web3j.abi.datatypes.primitive.Float;
import org.web3j.abi.datatypes.primitive.Int;
import org.web3j.abi.datatypes.primitive.Long;
import org.web3j.abi.datatypes.primitive.Short;
import org.web3j.codegen.GenerationReporter;
import org.web3j.codegen.LogGenerationReporter;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.codegen.generators.poets.*;
import org.web3j.codegen.generators.poets.TuplePoet;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Strings;
import org.web3j.utils.Version;

import static org.web3j.codegen.generators.SolidityConstants.*;

/** Generate Java Classes based on generated Solidity bin and abi files. */
public class SolidityWrapperGenerator extends Generator {

    private static final String CODEGEN_WARNING =
            "<p>Auto generated code.\n"
                    + "<p><strong>Do not modify!</strong>\n"
                    + "<p>Please use the "
                    + "<a href=\"https://docs.web3j.io/command_line.html\">web3j command line tools</a>,\n"
                    + "or the "
                    + SolidityFunctionWrapperGenerator.class.getName()
                    + " in the \n"
                    + "<a href=\"https://github.com/web3j/web3j/tree/master/codegen\">"
                    + "codegen module</a> to update.\n";

    private static final String regex = "(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?";
    private static final Pattern pattern = Pattern.compile(regex);

    private final SolidityWrapperGeneratorConfig config;

    public SolidityWrapperGeneratorConfig getConfig() {
        return config;
    }

    public SolidityWrapperGenerator(boolean useNativeJavaTypes) {
        this(useNativeJavaTypes, Address.DEFAULT_LENGTH);
    }

    public SolidityWrapperGenerator(boolean useNativeJavaTypes, int addressLength) {
        this(useNativeJavaTypes, false, false, addressLength);
    }

    public SolidityWrapperGenerator(
            boolean useNativeJavaTypes, int addressLength, boolean generateSendTxForCalls) {
        this(useNativeJavaTypes, generateSendTxForCalls, false, addressLength);
    }

    public SolidityWrapperGenerator(
            boolean useNativeJavaTypes,
            boolean useJavaPrimitiveTypes,
            boolean generateSendTxForCalls,
            int addressLength) {
        this(
                useNativeJavaTypes,
                useJavaPrimitiveTypes,
                generateSendTxForCalls,
                addressLength,
                new LogGenerationReporter(LOGGER));
    }

    public SolidityWrapperGenerator(
            boolean useNativeJavaTypes,
            boolean useJavaPrimitiveTypes,
            boolean generateSendTxForCalls,
            int addressLength,
            GenerationReporter reporter) {

        this.config =
                new SolidityWrapperGeneratorConfig(
                        generateSendTxForCalls,
                        useNativeJavaTypes,
                        useJavaPrimitiveTypes,
                        addressLength,
                        reporter);
    }

    public void generateJavaFiles(
            String contractName,
            String bin,
            List<AbiDefinition> abi,
            String destinationDir,
            String basePackageName,
            Map<String, String> addresses)
            throws IOException, ClassNotFoundException {

        generateJavaFiles(
                Contract.class, contractName, bin, abi, destinationDir, basePackageName, addresses);
    }

    public void generateJavaFiles(
            Class<? extends Contract> contractClass,
            String contractName,
            String bin,
            List<AbiDefinition> abi,
            String destinationDir,
            String basePackageName,
            Map<String, String> addresses)
            throws IOException, ClassNotFoundException {

        if (!java.lang.reflect.Modifier.isAbstract(contractClass.getModifiers())) {
            throw new IllegalArgumentException("Contract base class must be abstract");
        }

        String className = Strings.capitaliseFirstLetter(contractName);
        TypeSpec.Builder classBuilder = createClassBuilder(contractClass, className, bin);

        classBuilder.addAnnotation(
                AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "\"rawtypes\"")
                        .build());

        classBuilder.addMethod(
                ConstructorPoet.buildConstructor(Credentials.class, CREDENTIALS, false));

        classBuilder.addMethod(
                ConstructorPoet.buildConstructor(Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(
                ConstructorPoet.buildConstructor(
                        TransactionManager.class, TRANSACTION_MANAGER, false));
        classBuilder.addMethod(
                ConstructorPoet.buildConstructor(
                        TransactionManager.class, TRANSACTION_MANAGER, true));

        classBuilder.addFields(FunctionNamePoet.buildFunctionNameConstants(abi));

        classBuilder.addTypes(TuplePoet.buildTupleDefinitions(abi));

        classBuilder.addMethods(
                new FunctionPoet(config).buildFunctionDefinitions(className, classBuilder, abi));
        classBuilder.addMethods(new EventPoet(config).buildEventFunctions(classBuilder, abi));

        classBuilder.addMethod(
                LoadPoet.buildLoad(className, Credentials.class, CREDENTIALS, false));
        classBuilder.addMethod(
                LoadPoet.buildLoad(
                        className, TransactionManager.class, TRANSACTION_MANAGER, false));
        classBuilder.addMethod(LoadPoet.buildLoad(className, Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(
                LoadPoet.buildLoad(className, TransactionManager.class, TRANSACTION_MANAGER, true));

        if (!bin.equals(Contract.BIN_NOT_PROVIDED)) {
            classBuilder.addMethods(new DeployPoet(config).buildDeployMethods(className, abi));
        }

        addAddressesSupport(classBuilder, addresses);

        write(basePackageName, classBuilder.build(), destinationDir);
    }

    private void addAddressesSupport(TypeSpec.Builder classBuilder, Map<String, String> addresses) {
        if (addresses != null) {

            ClassName stringType = ClassName.get(String.class);
            ClassName mapType = ClassName.get(HashMap.class);
            TypeName mapStringString = ParameterizedTypeName.get(mapType, stringType, stringType);
            FieldSpec addressesStaticField =
                    FieldSpec.builder(
                                    mapStringString,
                                    "_addresses",
                                    Modifier.PROTECTED,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .build();
            classBuilder.addField(addressesStaticField);

            final CodeBlock.Builder staticInit = CodeBlock.builder();
            staticInit.addStatement("_addresses = new HashMap<String, String>()");
            addresses.forEach(
                    (k, v) ->
                            staticInit.addStatement(
                                    String.format("_addresses.put(\"%1s\", \"%2s\")", k, v)));
            classBuilder.addStaticBlock(staticInit.build());

            // See org.web3j.tx.Contract#getStaticDeployedAddress(String)
            MethodSpec getAddress =
                    MethodSpec.methodBuilder("getStaticDeployedAddress")
                            .addModifiers(Modifier.PROTECTED)
                            .returns(stringType)
                            .addParameter(stringType, "networkId")
                            .addCode(
                                    CodeBlock.builder()
                                            .addStatement("return _addresses.get(networkId)")
                                            .build())
                            .build();
            classBuilder.addMethod(getAddress);

            MethodSpec getPreviousAddress =
                    MethodSpec.methodBuilder("getPreviouslyDeployedAddress")
                            .addModifiers(Modifier.PUBLIC)
                            .addModifiers(Modifier.STATIC)
                            .returns(stringType)
                            .addParameter(stringType, "networkId")
                            .addCode(
                                    CodeBlock.builder()
                                            .addStatement("return _addresses.get(networkId)")
                                            .build())
                            .build();
            classBuilder.addMethod(getPreviousAddress);
        }
    }

    private TypeSpec.Builder createClassBuilder(
            Class<? extends Contract> contractClass, String className, String binary) {

        String javadoc = CODEGEN_WARNING + getWeb3jVersion();

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(javadoc)
                .superclass(contractClass)
                .addField(createBinaryDefinition(binary));
    }

    private String getWeb3jVersion() {
        String version;

        try {
            // This only works if run as part of the web3j command line tools which contains
            // a version.properties file
            version = Version.getVersion();
        } catch (IOException | NullPointerException e) {
            version = Version.DEFAULT;
        }
        return "\n<p>Generated with web3j version " + version + ".\n";
    }

    private FieldSpec createBinaryDefinition(String binary) {
        return FieldSpec.builder(String.class, BINARY)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", binary)
                .build();
    }

    public static TypeName getNativeType(TypeName typeName) {

        if (typeName instanceof ParameterizedTypeName) {
            return getNativeType((ParameterizedTypeName) typeName);
        }

        String simpleName = ((ClassName) typeName).simpleName();

        if (simpleName.equals(Address.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Uint")) {
            return TypeName.get(BigInteger.class);
        } else if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Bytes") || simpleName.equals("DynamicBytes")) {
            return TypeName.get(byte[].class);
        } else if (simpleName.startsWith("Bool")) {
            return TypeName.get(java.lang.Boolean.class);
            // boolean cannot be a parameterized type
        } else if (simpleName.equals(Byte.class.getSimpleName())) {
            return TypeName.get(java.lang.Byte.class);
        } else if (simpleName.equals(Char.class.getSimpleName())) {
            return TypeName.get(Character.class);
        } else if (simpleName.equals(Double.class.getSimpleName())) {
            return TypeName.get(java.lang.Double.class);
        } else if (simpleName.equals(Float.class.getSimpleName())) {
            return TypeName.get(java.lang.Float.class);
        } else if (simpleName.equals(Int.class.getSimpleName())) {
            return TypeName.get(Integer.class);
        } else if (simpleName.equals(Long.class.getSimpleName())) {
            return TypeName.get(java.lang.Long.class);
        } else if (simpleName.equals(Short.class.getSimpleName())) {
            return TypeName.get(java.lang.Short.class);
        } else if (simpleName.startsWith("Int")) {
            return TypeName.get(BigInteger.class);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported type: " + typeName + ", no native type mapping exists.");
        }
    }

    static TypeName getNativeType(ParameterizedTypeName parameterizedTypeName) {
        List<TypeName> typeNames = parameterizedTypeName.typeArguments;
        List<TypeName> nativeTypeNames = new ArrayList<>(typeNames.size());
        for (TypeName enclosedTypeName : typeNames) {
            nativeTypeNames.add(getNativeType(enclosedTypeName));
        }
        return ParameterizedTypeName.get(
                ClassName.get(List.class),
                nativeTypeNames.toArray(new TypeName[nativeTypeNames.size()]));
    }

    public static List<TypeName> buildTypeNames(
            List<AbiDefinition.NamedType> namedTypes, boolean primitives)
            throws ClassNotFoundException {

        List<TypeName> result = new ArrayList<>(namedTypes.size());
        for (AbiDefinition.NamedType namedType : namedTypes) {
            result.add(buildTypeName(namedType.getType(), namedType.getComponents(), primitives));
        }
        return result;
    }

    public static TypeName buildTypeName(String typeDeclaration) throws ClassNotFoundException {
        return buildTypeName(typeDeclaration, null, false);
    }

    public static TypeName buildTypeName(
            String typeDeclaration, List<AbiDefinition.NamedTypeComponent> components)
            throws ClassNotFoundException {
        return buildTypeName(typeDeclaration, components, false);
    }

    public static TypeName buildTypeName(
            String typeDeclaration,
            List<AbiDefinition.NamedTypeComponent> components,
            boolean primitives)
            throws ClassNotFoundException {
        if ("tuple".equals(typeDeclaration)) {
            // There must be a components argument!
            if (components == null || components.size() == 0) {
                throw new IllegalArgumentException("Components must be provided for 'tuples'");
            }

            // TODO[Sam]: sort out package name as parameter.
            final String tupleClassName = TuplePoet.buildClassNameForTupleComponents(components);

            // TODO: determine if static or dynamic struct?
            return ClassName.get("", "ComplexStorage." + tupleClassName);
        } else {
            final String solidityType = trimStorageDeclaration(typeDeclaration);

            final TypeReference typeReference =
                    TypeReference.makeTypeReference(solidityType, false, primitives);

            return TypeName.get(typeReference.getType());
        }
    }

    private static Class<?> getStaticArrayTypeReferenceClass(String type) {
        try {
            return Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + type);
        } catch (ClassNotFoundException e) {
            // Unfortunately we can't encode it's length as a type if it's > 32.
            return StaticArray.class;
        }
    }

    private static String trimStorageDeclaration(String type) {
        if (type.endsWith(" storage") || type.endsWith(" memory")) {
            return type.split(" ")[0];
        } else {
            return type;
        }
    }

    public static TypeName getWrapperType(
            TypeName typeName,
            List<AbiDefinition.NamedTypeComponent> components,
            boolean useNativeJavaTypes) {
        // TODO: re-instate this!
        if (((ClassName) typeName).simpleName().contains("TupleClass")) {
            // TODO: do this in a better way!
            // Re-assign typeName to TupleClass<N>.
            // TODO: sort out package name as parameter.
            if (components == null || components.size() == 0) {
                throw new IllegalArgumentException("Components must be provided for tuples");
            }
            final String tupleClassName = TuplePoet.buildClassNameForTupleComponents(components);
            return ClassName.get("", "ComplexStorage." + tupleClassName);
        } else {
            if (useNativeJavaTypes) {
                return getNativeType(typeName);
            } else {
                return typeName;
            }
        }
    }

    private List<AbiDefinition> loadContractDefinition(String abi) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(abi, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    public static class NamedTypeName {
        public final TypeName typeName;
        private final String name;
        private final boolean indexed;
        private final List<AbiDefinition.NamedTypeComponent> components;

        public NamedTypeName(
                String name,
                TypeName typeName,
                boolean indexed,
                List<AbiDefinition.NamedTypeComponent> components) {
            this.name = name;
            this.typeName = typeName;
            this.indexed = indexed;
            this.components = components;
        }

        public String getName() {
            return name;
        }

        public TypeName getTypeName() {
            return typeName;
        }

        public boolean isIndexed() {
            return indexed;
        }

        public List<AbiDefinition.NamedTypeComponent> getComponents() {
            return components;
        }
    }
}
