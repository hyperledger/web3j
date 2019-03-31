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
public class StaticArray105<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray105(List<T> values) {
        super(105, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray105(T... values) {
        super(105, values);
    }

    public StaticArray105(Class<T> type, List<T> values) {
        super(type, 105, values);
    }

    @SafeVarargs
    public StaticArray105(Class<T> type, T... values) {
        super(type, 105, values);
    }
}
