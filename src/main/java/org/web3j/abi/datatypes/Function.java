package org.web3j.abi.datatypes;

import java.util.Arrays;
import java.util.List;

import org.web3j.abi.FunctionEncoder;

/**
 * Function wrapper
 */
public class Function {
    private String name;
    private List<Type> parameters;

    public Function(String name, Type... parameters) {
        this.name = name;
        this.parameters = Arrays.asList(parameters);
    }

    public String getName() {
        return name;
    }

    public List<Type> getParameters() {
        return parameters;
    }
}
