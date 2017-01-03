package org.web3j.codegen;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import org.web3j.abi.Contract;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.*;
import org.web3j.utils.Collection;
import org.web3j.utils.Strings;

import static org.web3j.utils.Collection.tail;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class SolidityFunctionWrapperGenerator {

    private static final String BINARY = "BINARY";
    private static final String WEB3J = "web3j";
    private static final String CREDENTIALS = "credentials";
    private static final String INITIAL_VALUE = "initialValue";
    private static final String CONTRACT_ADDRESS = "contractAddress";
    private static final String GAS_PRICE = "gasPrice";
    private static final String GAS_LIMIT = "gasLimit";

    private static final String CODEGEN_WARNING = "<p>Auto generated code.<br>\n" +
            "<strong>Do not modify!</strong><br>\n" +
            "Please use {@link " + SolidityFunctionWrapperGenerator.class.getName() +
            "} to update.</p>\n";

    private static final String USAGE = "solidity generate " +
            "<input binary file>.bin <input abi file>.abi " +
            "[-p|--package <base package name>] " +
            "-o|--output <destination base directory>";

    private String binaryFileLocation;
    private String absFileLocation;
    private File destinationDirLocation;
    private String basePackageName;

    private SolidityFunctionWrapperGenerator(
            String binaryFileLocation,
            String absFileLocation,
            String destinationDirLocation,
            String basePackageName) {

        this.binaryFileLocation = binaryFileLocation;
        this.absFileLocation = absFileLocation;
        this.destinationDirLocation = new File(destinationDirLocation);
        this.basePackageName = basePackageName;
    }

    public static void run(String[] args) throws Exception {
        if (args.length < 1 || !args[0].equals("generate")) {
            exitError(USAGE);
        } else {
            main(tail(args));
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 6) {
            exitError(USAGE);
        }

        String binaryFileLocation = parsePositionalArg(args, 0);
        String absFileLocation = parsePositionalArg(args, 1);
        String destinationDirLocation = parseParameterArgument(args, "-o", "--outputDir");
        String basePackageName = parseParameterArgument(args, "-p", "--package");

        if (binaryFileLocation.equals("")
                || absFileLocation.equals("")
                || destinationDirLocation.equals("")
                || basePackageName.equals("")) {
            exitError(USAGE);
        }

        new SolidityFunctionWrapperGenerator(
                binaryFileLocation,
                absFileLocation,
                destinationDirLocation,
                basePackageName)
                .generate();
    }

    private static String parsePositionalArg(String[] args, int idx) {
        if (args != null && args.length > idx) {
            return args[idx];
        } else {
            return "";
        }
    }

    private static String parseParameterArgument(String[] args, String... parameters) {
        for (String parameter:parameters) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(parameter)
                        && i + 1 < args.length) {
                    String parameterValue = args[i+1];
                    if (!parameterValue.startsWith("-")) {
                        return parameterValue;
                    }
                }
            }
        }
        return "";
    }

    private static void exitError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private void generate() throws IOException, ClassNotFoundException {

        File binaryFile = new File(binaryFileLocation);
        if (!binaryFile.exists()) {
            exitError("Invalid input binary file specified: " + binaryFileLocation);
        }

        byte[] bytes = Files.readBytes(new File(binaryFile.toURI()));
        String binary = new String(bytes);

        File absFile = new File(absFileLocation);
        if (!absFile.exists() || !absFile.canRead()) {
            exitError("Invalid input ABI file specified: " + absFileLocation);
        }
        String fileName = absFile.getName();
        String contractName = getFileNameNoExtension(fileName);

        List<AbiDefinition> functionDefinitions = loadContractDefinition(absFile);

        if (functionDefinitions.isEmpty()) {
            exitError("Unable to parse input ABI file");
        } else {
            generateSolidityWrappers(binary, contractName, functionDefinitions, basePackageName);
        }
    }

    private static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    static String getFileNameNoExtension(String fileName) {
        String[] splitName = fileName.split("\\.(?=[^.]*$)");
        return splitName[0];
    }

    private void generateSolidityWrappers(
            String binary, String contractName, List<AbiDefinition> functionDefinitions,
            String basePackageName) throws IOException, ClassNotFoundException {

        String className = Strings.capitaliseFirstLetter(contractName);

        TypeSpec.Builder classBuilder = createClassBuilder(className, binary);
        classBuilder.addMethod(buildConstructor());
        classBuilder.addMethods(buildFunctionDefinitions(className, functionDefinitions));
        classBuilder.addMethod(buildLoad(className));

        System.out.printf("Generating " + basePackageName + "." + className + " ... ");
        JavaFile javaFile = JavaFile.builder(basePackageName, classBuilder.build())
                .indent("    ")  // default indentation is two spaces
                .build();
        System.out.println("complete");

        javaFile.writeTo(destinationDirLocation);
        System.out.println("File written to " + destinationDirLocation.toString() + "\n");
    }

    private TypeSpec.Builder createClassBuilder(String className, String binary) {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(CODEGEN_WARNING)
                .superclass(Contract.class)
                .addField(createBinaryDefinition(binary));
    }

    private FieldSpec createBinaryDefinition(String binary) {
        return FieldSpec.builder(String.class, BINARY)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", binary)
                .build();
    }

    private static List<MethodSpec> buildFunctionDefinitions(
            String className,
            List<AbiDefinition> functionDefinitions) throws ClassNotFoundException {

        List<MethodSpec> methodSpecs = new ArrayList<MethodSpec>();
        boolean constructor = false;

        for (AbiDefinition functionDefinition:functionDefinitions) {
            if (functionDefinition.getType().equals("function")) {
                methodSpecs.add(buildFunction(functionDefinition));

            } else if (functionDefinition.getType().equals("event")) {
                methodSpecs.add(buildEventFunction(functionDefinition));

            } else if (functionDefinition.getType().equals("constructor")) {
                constructor = true;
                methodSpecs.add(buildDeploy(className, functionDefinition));
            }
        }

        // constructor will not be specified in ABI file if its empty
        if (!constructor) {
            MethodSpec.Builder methodBuilder = getDeployMethodSpec(className);
            methodSpecs.add(buildDeployAsyncNoParams(methodBuilder, className));
        }

        return methodSpecs;
    }

    private static MethodSpec buildConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(String.class, CONTRACT_ADDRESS)
                .addParameter(Web3j.class, WEB3J)
                .addParameter(Credentials.class, CREDENTIALS)
                .addParameter(BigInteger.class, GAS_PRICE)
                .addParameter(BigInteger.class, GAS_LIMIT)
                .addStatement("super($N, $N, $N, $N, $N)",
                        CONTRACT_ADDRESS, WEB3J, CREDENTIALS, GAS_PRICE, GAS_LIMIT)
                .build();
    }

    private static MethodSpec buildDeploy(
            String className, AbiDefinition functionDefinition) {

        MethodSpec.Builder methodBuilder = getDeployMethodSpec(className);
        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        if (!inputParams.isEmpty()) {
            return buildDeployAsyncWithParams(methodBuilder, className, inputParams);
        } else {
            return buildDeployAsyncNoParams(methodBuilder, className);
        }
    }

    private static MethodSpec buildDeployAsyncWithParams(
            MethodSpec.Builder methodBuilder, String className, String inputParams) {
        methodBuilder.addStatement("$T encodedConstructor = $T.encodeConstructor(" +
                        "$T.<$T>asList($L)" +
                        ")",
                String.class, FunctionEncoder.class, Arrays.class, Type.class, inputParams);
        methodBuilder.addStatement(
                "return deployAsync($L.class, $L, $L, $L, $L, $L, encodedConstructor, $L)",
                className, WEB3J, CREDENTIALS, GAS_PRICE, GAS_LIMIT, BINARY, INITIAL_VALUE);
        return methodBuilder.build();
    }

    private static MethodSpec buildDeployAsyncNoParams(
            MethodSpec.Builder methodBuilder, String className) {
        methodBuilder.addStatement("return deployAsync($L.class, $L, $L, $L, $L, $L, \"\", $L)",
                className, WEB3J, CREDENTIALS, GAS_PRICE, GAS_LIMIT, BINARY, INITIAL_VALUE);
        return methodBuilder.build();
    }

    private static MethodSpec.Builder getDeployMethodSpec(String className) {
        return MethodSpec.methodBuilder("deploy")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(
                        ClassName.get(Future.class), TypeVariableName.get(className, Type.class)))
                .addParameter(Web3j.class, WEB3J)
                .addParameter(Credentials.class, CREDENTIALS)
                .addParameter(BigInteger.class, GAS_PRICE)
                .addParameter(BigInteger.class, GAS_LIMIT)
                .addParameter(BigInteger.class, INITIAL_VALUE);
    }

    private static MethodSpec buildLoad(String className) {
        return MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeVariableName.get(className, Type.class))
                .addParameter(String.class, CONTRACT_ADDRESS)
                .addParameter(Web3j.class, WEB3J)
                .addParameter(Credentials.class, CREDENTIALS)
                .addParameter(BigInteger.class, GAS_PRICE)
                .addParameter(BigInteger.class, GAS_LIMIT)
                .addStatement("return new $L($L, $L, $L, $L, $L)",
                        className, CONTRACT_ADDRESS, WEB3J, CREDENTIALS, GAS_PRICE, GAS_LIMIT)
                .build();
    }

    static String addParameters(
            MethodSpec.Builder methodBuilder, List<AbiDefinition.NamedType> namedTypes) {

        List<ParameterSpec> inputParameterTypes = buildParameterTypes(namedTypes);
        methodBuilder.addParameters(inputParameterTypes);

        return org.web3j.utils.Collection.join(
                inputParameterTypes,
                ", ",
                new Collection.Function<ParameterSpec, String>() {
                    @Override
                    public String apply(ParameterSpec parameterSpec) {
                        return parameterSpec.name;
                    }
                });
    }

    static List<ParameterSpec> buildParameterTypes(List<AbiDefinition.NamedType> namedTypes) {
        List<ParameterSpec> result = new ArrayList<ParameterSpec>(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); i++) {
            AbiDefinition.NamedType namedType = namedTypes.get(i);

            String name = createValidParamName(namedType.getName(), i);
            String type = namedTypes.get(i).getType();

            result.add(ParameterSpec.builder(buildTypeName(type), name).build());
        }
        return result;
    }

    /**
     * Public Solidity arrays and maps require an unnamed input parameter - multiple if they
     * require a struct type
     *
     * @param name
     * @param idx
     * @return non-empty parameter name
     */
    static String createValidParamName(String name, int idx) {
        if (name.equals("")) {
            return "param" + idx;
        } else {
            return name;
        }
    }

    static List<TypeName> buildTypeNames(List<AbiDefinition.NamedType> namedTypes) {
        List<TypeName> result = new ArrayList<TypeName>(namedTypes.size());
        for (AbiDefinition.NamedType namedType : namedTypes) {
            result.add(buildTypeName(namedType.getType()));
        }
        return result;
    }

    static MethodSpec buildFunction(
            AbiDefinition functionDefinition) throws ClassNotFoundException {
        String functionName = functionDefinition.getName();

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(functionName)
                        .addModifiers(Modifier.PUBLIC);

        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        List<TypeName> outputParameterTypes = buildTypeNames(functionDefinition.getOutputs());
        if (functionDefinition.isConstant()) {
            buildConstantFunction(
                    functionDefinition, methodBuilder, outputParameterTypes, inputParams);
        } else {
            buildTransactionFunction(
                    functionDefinition, methodBuilder, inputParams);
        }

        return methodBuilder.build();
    }

    private static void buildConstantFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            List<TypeName> outputParameterTypes,
            String inputParams) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();

        if (outputParameterTypes.isEmpty()) {
            throw new RuntimeException("Only transactional methods should have void return types");
        } else if (outputParameterTypes.size() == 1) {
            methodBuilder.returns(ParameterizedTypeName.get(
                    ClassName.get(Future.class), outputParameterTypes.get(0)));

            TypeName typeName = outputParameterTypes.get(0);
            methodBuilder.addStatement("$T function = " +
                            "new $T($S, \n$T.<$T>asList($L), \n$T.<$T<?>>asList(new $T<$T>() {}))",
                    Function.class, Function.class, functionName,
                    Arrays.class, Type.class, inputParams,
                    Arrays.class, TypeReference.class,
                    TypeReference.class, typeName);
            methodBuilder.addStatement("return executeCallSingleValueReturnAsync(function)");

        } else {
            methodBuilder.returns(ParameterizedTypeName.get(
                    ClassName.get(Future.class),
                    ParameterizedTypeName.get(
                            ClassName.get(List.class), ClassName.get(Type.class))));

            buildVariableLengthReturnFunctionConstructor(
                    methodBuilder, functionName, inputParams, outputParameterTypes);

            methodBuilder.addStatement("return executeCallMultipleValueReturnAsync(function)");
        }
    }

    private static void buildTransactionFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            String inputParams) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();

        methodBuilder.returns(ParameterizedTypeName.get(Future.class, TransactionReceipt.class));

        methodBuilder.addStatement("$T function = new $T($S, $T.<$T>asList($L), $T.<$T<?>>emptyList())",
                Function.class, Function.class, functionName,
                Arrays.class, Type.class, inputParams, Collections.class,
                TypeReference.class);
        methodBuilder.addStatement("return executeTransactionAsync(function)");

        return methodBuilder;
    }

    private static void buildEventFunction(
            AbiDefinition functionDefinition) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();
        String generatedFunctionName =
                "process" + Strings.capitaliseFirstLetter(functionName) + "Event";

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(generatedFunctionName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TransactionReceipt.class, "transactionReceipt")
                .returns(EventValues.class);

        List<AbiDefinition.NamedType> inputs = functionDefinition.getInputs();

        List<TypeName> indexedParameters = new ArrayList<TypeName>();
        List<TypeName> nonIndexedParameters = new ArrayList<TypeName>();

        for (AbiDefinition.NamedType namedType:inputs) {
            if (namedType.isIndexed()) {
                indexedParameters.add(buildTypeName(namedType.getType()));
            } else {
                nonIndexedParameters.add(buildTypeName(namedType.getType()));
            }
        }

        buildVariableLengthEventConstructor(
                methodBuilder, functionName, indexedParameters, nonIndexedParameters);

        return methodBuilder
                .addStatement("return extractEventParameters(event, transactionReceipt)")
                .build();
    }

    static TypeName buildTypeName(String type) {
        if (type.endsWith("]")) {
            String[] splitType = type.split("\\[");
            Class<?> baseType = AbiTypes.getType(splitType[0]);

            TypeName typeName;
            if (splitType[1].length() == 1) {
                typeName = ParameterizedTypeName.get(DynamicArray.class, baseType);
            } else {
                // Unfortunately we can't encode it's length as a type
                typeName = ParameterizedTypeName.get(StaticArray.class, baseType);
            }
            return typeName;
        } else {
            Class<?> cls = AbiTypes.getType(type);
            return ClassName.get(cls);
        }
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            MethodSpec.Builder methodBuilder, String functionName, String inputParameters,
            List<TypeName> outputParameterTypes) throws ClassNotFoundException {

        List<Object> objects = new ArrayList<Object>();
        objects.add(Function.class);
        objects.add(Function.class);
        objects.add(functionName);

        objects.add(Arrays.class);
        objects.add(Type.class);
        objects.add(inputParameters);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (TypeName outputParameterType: outputParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(outputParameterType);
        }

        String asListParams = Collection.join(
                outputParameterTypes,
                ", ",
                new Collection.Function<TypeName, String>() {
                    @Override
                    public String apply(TypeName typeName) {
                        return "new $T<$T>() {}";
                    }
                });

        methodBuilder.addStatement("$T function = new $T($S, \n$T.<$T>asList($L), \n$T.<$T<?>>asList(" +
                asListParams + "))", objects.toArray());
    }

    private static void buildVariableLengthEventConstructor(
            MethodSpec.Builder methodBuilder, String eventName, List<TypeName> indexedParameterTypes,
            List<TypeName> nonIndexedParameterTypes) throws ClassNotFoundException {

        List<Object> objects = new ArrayList<Object>();
        objects.add(Event.class);
        objects.add(Event.class);
        objects.add(eventName);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (TypeName indexedParameterType: indexedParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(indexedParameterType);
        }

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (TypeName indexedParameterType: nonIndexedParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(indexedParameterType);
        }

        String indexedAsListParams = Collection.join(
                indexedParameterTypes,
                ", ",
                new Collection.Function<TypeName, String>() {
                    @Override
                    public String apply(TypeName typeName) {
                        return "new $T<$T>() {}";
                    }
                });

        String nonIndexedAsListParams = Collection.join(
                nonIndexedParameterTypes,
                ", ",
                new Collection.Function<TypeName, String>() {
                    @Override
                    public String apply(TypeName typeName) {
                        return "new $T<$T>() {}";
                    }
                });

        methodBuilder.addStatement("$T event = new $T($S, \n" +
                "$T.<$T<?>>asList(" + indexedAsListParams + "),\n" +
                "$T.<$T<?>>asList(" + nonIndexedAsListParams + "))", objects.toArray());
    }
}
