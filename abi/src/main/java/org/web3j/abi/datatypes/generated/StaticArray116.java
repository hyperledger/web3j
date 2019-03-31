package org.web3j.abi.datatypes.generated;

import java.util.List;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray116<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray116(List<T> values) {
        super(116, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray116(T... values) {
        super(116, values);
    }

    public StaticArray116(Class<T> type, List<T> values) {
        super(type, 116, values);
    }

    @SafeVarargs
    public StaticArray116(Class<T> type, T... values) {
        super(type, 116, values);
    }
}
