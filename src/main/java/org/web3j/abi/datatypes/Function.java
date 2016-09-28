package org.web3j.abi.datatypes;

import java.util.List;

/**
 * Function wrapper
 */
public class Function<T extends Type> {
    private String name;
    private List<Type> inputParameters;
    private List<Class<T>> outputParameters;

    public Function(String name, List<Type> inputParameters, List<Class<T>> outputParameters) {
        this.name = name;
        this.inputParameters = inputParameters;
        this.outputParameters = outputParameters;
    }

    public String getName() {
        return name;
    }

    public List<Type> getInputParameters() {
        return inputParameters;
    }

    public List<Class<T>> getOutputParameters() {
        return outputParameters;
    }
}
