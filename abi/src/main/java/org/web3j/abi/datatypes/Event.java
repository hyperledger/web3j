package org.web3j.abi.datatypes;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import org.web3j.abi.TypeReference;

import static org.web3j.abi.Utils.convert;

/**
 * Event wrapper type.
 */
public class Event {
    private String name;
    private List<TypeReference<Type>> parameters;

    public Event(String name, List<TypeReference<?>> parameters) {
        this.name = name;
        this.parameters = convert(parameters);
    }

    public String getName() {
        return name;
    }

    public List<TypeReference<Type>> getParameters() {
        return parameters;
    }

    public List<TypeReference<Type>> getIndexedParameters() {
        return StreamSupport.stream(parameters)
                .filter(TypeReference::isIndexed)
                .collect(Collectors.toList());
    }

    public List<TypeReference<Type>> getNonIndexedParameters() {
        return StreamSupport.stream(parameters)
                .filter(p -> !p.isIndexed())
                .collect(Collectors.toList());
    }
}
