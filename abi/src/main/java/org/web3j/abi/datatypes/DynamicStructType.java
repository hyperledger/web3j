package org.web3j.abi.datatypes;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicStructType extends DynamicArray<Type> implements BasicStruct {


    // TODO: make this private and expose some accessors or wahtever?
    protected final List<Class<Type>> itemTypes = new ArrayList<>();

    public DynamicStructType(List<Type> values) {
        this(Type.class, values);
    }

    // TODO: remove this constructor!
    public DynamicStructType(Class<Type> type, List<Type> values) {
        super(type, values);
        for (Type value : values) {
            itemTypes.add((Class<Type>) value.getClass());
        }
    }

    public DynamicStructType(Type... values) {
        this(Arrays.asList(values));
    }


    @SafeVarargs
    public DynamicStructType(Class<Type> type, Type... values) {
        this(type, Arrays.asList(values));
    }

    @Override
    public String getTypeAsString() {
        // "(uint256,string)"
        final StringBuilder type = new StringBuilder("(");
        type.append(itemTypes.stream().map(t -> AbiTypes.getTypeAString(t)).collect(Collectors.joining(",")));
        type.append(")");
        return type.toString();
    }
}
