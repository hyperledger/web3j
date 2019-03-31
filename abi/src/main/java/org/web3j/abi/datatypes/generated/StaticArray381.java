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
public class StaticArray381<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray381(List<T> values) {
        super(381, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray381(T... values) {
        super(381, values);
    }

    public StaticArray381(Class<T> type, List<T> values) {
        super(type, 381, values);
    }

    @SafeVarargs
    public StaticArray381(Class<T> type, T... values) {
        super(type, 381, values);
    }
}
