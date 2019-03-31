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
public class StaticArray168<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray168(List<T> values) {
        super(168, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray168(T... values) {
        super(168, values);
    }

    public StaticArray168(Class<T> type, List<T> values) {
        super(type, 168, values);
    }

    @SafeVarargs
    public StaticArray168(Class<T> type, T... values) {
        super(type, 168, values);
    }
}
