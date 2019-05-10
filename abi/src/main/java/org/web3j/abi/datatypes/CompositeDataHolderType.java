package org.web3j.abi.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeDataHolderType extends DynamicStructType {

    // TODO: make this private and expose some accessors or wahtever?
    protected final List<Class<Type>> itemTypes = new ArrayList<>();

    public CompositeDataHolderType(List<Type> values) {
        super(Type.class, values);
        for (Type value : values) {
            itemTypes.add((Class<Type>) value.getClass());
        }
    }

    @SafeVarargs
    public CompositeDataHolderType(Type... values) {
        this(Arrays.asList(values));
    }

}
