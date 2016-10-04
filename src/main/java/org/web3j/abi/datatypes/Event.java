package org.web3j.abi.datatypes;

import java.util.List;

/**
 * Event wrapper
 */
public class Event<T extends Type> {
    private String name;
    private List<Class<T>> indexedParameters;
    private List<Class<T>> nonIndexedParameters;

    public Event(String name, List<Class<T>> indexedParameters, List<Class<T>> nonIndexedParameters) {
        this.name = name;
        this.indexedParameters = indexedParameters;
        this.nonIndexedParameters = nonIndexedParameters;
    }

    public String getName() {
        return name;
    }

    public List<Class<T>> getIndexedParameters() {
        return indexedParameters;
    }

    public List<Class<T>> getNonIndexedParameters() {
        return nonIndexedParameters;
    }
}
