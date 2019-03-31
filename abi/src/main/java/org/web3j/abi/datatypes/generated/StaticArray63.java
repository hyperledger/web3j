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
public class StaticArray63<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray63(List<T> values) {
        super(63, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray63(T... values) {
        super(63, values);
    }

    public StaticArray63(Class<T> type, List<T> values) {
        super(type, 63, values);
    }

    @SafeVarargs
    public StaticArray63(Class<T> type, T... values) {
        super(type, 63, values);
    }
}
