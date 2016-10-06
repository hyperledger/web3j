package org.web3j.abi.datatypes;

import java.util.List;

import org.web3j.abi.TypeReference;

/**
 * Event wrapper
 */
public class Event<T extends Type> {
    private String name;
    private List<TypeReference<T>> indexedParameters;
    private List<TypeReference<T>> nonIndexedParameters;

    public Event(String name, List<TypeReference<T>> indexedParameters,
                 List<TypeReference<T>> nonIndexedParameters) {
        this.name = name;
        this.indexedParameters = indexedParameters;
        this.nonIndexedParameters = nonIndexedParameters;
    }

    public String getName() {
        return name;
    }

    public List<TypeReference<T>> getIndexedParameters() {
        return indexedParameters;
    }

    public List<TypeReference<T>> getNonIndexedParameters() {
        return nonIndexedParameters;
    }
}
