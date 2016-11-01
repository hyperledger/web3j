package org.web3j.abi.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.TypeReference;

/**
 * Event wrapper
 */
public class Event {
    private String name;
    private List<TypeReference<Type>> indexedParameters;
    private List<TypeReference<Type>> nonIndexedParameters;

    public Event(String name, List<TypeReference<?>> indexedParameters,
                 List<TypeReference<?>> nonIndexedParameters) {
        this.name = name;
        this.indexedParameters = convert(indexedParameters);
        this.nonIndexedParameters = convert(nonIndexedParameters);
    }

    public String getName() {
        return name;
    }

    public List<TypeReference<Type>> getIndexedParameters() {
        return indexedParameters;
    }

    public List<TypeReference<Type>> getNonIndexedParameters() {
        return nonIndexedParameters;
    }

    // TODO: We're going to need to use a similar approach for managing function return values
    // as the current function implementation is using Type as an upper type bound, which is not
    // sustainable if we want to support returning a mixture of any return values
    @SuppressWarnings("unchecked")
    private static List<TypeReference<Type>> convert(List<TypeReference<?>> input) {
        List<TypeReference<Type>> result = new ArrayList<>(input.size());
        result.addAll(input.stream()
                .map(typeReference -> (TypeReference<Type>) typeReference)
                .collect(Collectors.toList()));
        return result;
    }
}
