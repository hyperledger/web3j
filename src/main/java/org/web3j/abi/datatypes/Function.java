package org.web3j.abi.datatypes;

import java.util.Arrays;
import java.util.List;

/**
 * Function wrapper
 */
public class Function {
    private String name;
    private String signature;
    private List<Type> parameters;

    public Function(String name, String signature, Type... parameters) {
        this.name = name;
        this.signature = signature;
        this.parameters = Arrays.asList(parameters);
    }

    public String getName() {
        return name;
    }

    public String getFullSignature() {
        return name + signature;
    }

    public List<Type> getParameters() {
        return parameters;
    }
}
