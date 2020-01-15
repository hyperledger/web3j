package org.web3j.codegen.unit.gen;

import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.ParameterSpec;
import org.junit.jupiter.api.BeforeAll;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.web3j.codegen.unit.gen.utills.NameUtils.toCamelCase;

public class FunParser {
    private final Method method;
    private final Class theContract;

    FunParser(final Method method, final Class theContract) {
        this.method = method;
        this.theContract = theContract;
    }

    FunSpec getFunSpec() {
        return methodNeedsInjection()
                ? new FunSpecGenerator(
                method.getName(),
                BeforeAll.class,
                defaultParameterSpecsForEachUnitTest(),
                generateStatementBody())
                .generate()
                : new FunSpecGenerator(method.getName(), generateStatementBody()).generate();
    }

    private boolean methodNeedsInjection() {
        return Arrays.asList(method.getParameterTypes())
                .containsAll(
                        Arrays.asList(
                                Web3j.class, TransactionManager.class, ContractGasProvider.class));
    }

    private List<ParameterSpec> defaultParameterSpecsForEachUnitTest() {
        return Stream.of(
                ParameterSpec.builder(toCamelCase(Web3j.class), Web3j.class).build(),
                ParameterSpec.builder(toCamelCase(TransactionManager.class), TransactionManager.class).build(),
                ParameterSpec.builder(toCamelCase(ContractGasProvider.class), ContractGasProvider.class).build()).collect(Collectors.toList());
    }

    private Map<String, Object[]> generateStatementBody() {
        Map<String, Object[]> methodBodySpecification = new LinkedHashMap<>();
        String javaPoetStringTypes = KotlinParserUtils.generateKotlinPoetStringTypes(method, theContract);
        Object[] genericParameters = KotlinParserUtils.generatePlaceholderValues(method, theContract);
        methodBodySpecification.put(javaPoetStringTypes, genericParameters);
        if (methodNeedsAssertion()) {
            String assertionJavaPoet =
                    KotlinParserUtils.generateAssertionKotlinPoetStringTypes(method, theContract);
            Object[] assertionParams =
                    KotlinParserUtils.generateAssertionPlaceholderValues(method, theContract);
            methodBodySpecification.put(assertionJavaPoet, assertionParams);
        }
        return methodBodySpecification;
    }

    private boolean methodNeedsAssertion() {
        return !methodNeedsInjection();
    }
}
