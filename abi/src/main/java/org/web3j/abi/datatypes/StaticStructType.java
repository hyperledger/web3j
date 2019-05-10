package org.web3j.abi.datatypes;

import org.web3j.abi.datatypes.generated.AbiTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: provide a base interface called "StructType"
public class StaticStructType extends StaticArray<Type> implements BasicStruct {

    // TODO: make this private and expose some accessors or wahtever?
    protected final List<Class<Type>> itemTypes = new ArrayList<>();

    public StaticStructType(List<Type> values) {
        super(Type.class, values.size(), values);
        for (Type value : values) {
            itemTypes.add((Class<Type>) value.getClass());
        }
    }

    @SafeVarargs
    public StaticStructType(Type... values) {
        this(Arrays.asList(values));
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
