package org.web3j.codegen.unit.gen;


import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FunSpec;
import com.squareup.kotlinpoet.ParameterSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FunSpecGenerator {
    private final String testMethodName;
    private final Map<String, Object[]> statementBody;
    private final List<ParameterSpec> testMethodParameters;
    private final Class testMethodAnnotation;
    private final Modifier testMethodModifier;

    public FunSpecGenerator(
            String testMethodName,
            Class testMethodAnnotation,
            List<ParameterSpec> testMethodParameters,
            Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = testMethodAnnotation;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = testMethodParameters;
    }

    public FunSpecGenerator(String testMethodName, Map<String, Object[]> statementBody) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = Collections.emptyList();
    }

    public FunSpecGenerator(
            String testMethodName,
            Map<String, Object[]> statementBody,
            List<ParameterSpec> testMethodParameters) {
        this.statementBody = statementBody;
        this.testMethodName = testMethodName;
        this.testMethodAnnotation = Test.class;
        this.testMethodModifier = Modifier.PUBLIC;
        this.testMethodParameters = testMethodParameters;
    }

    public FunSpec generate() {
        return FunSpec.builder(testMethodName)
                .addAnnotation(testMethodAnnotation)
                .addParameters(testMethodParameters)
                .addCode(setMethodBody())
                .build();
    }

    private CodeBlock setMethodBody() {
        CodeBlock.Builder methodBody = CodeBlock.builder();
        statementBody.forEach(methodBody::addStatement);
        return methodBody.build();
    }
}
