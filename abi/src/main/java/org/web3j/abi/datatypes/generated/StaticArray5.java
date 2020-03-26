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
public class StaticArray5<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray5(final List<T> values) {
        super(5, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray5(final T... values) {
        super(5, values);
    }

    public StaticArray5(final Class<T> type, final List<T> values) {
        super(type, 5, values);
    }

    @SafeVarargs
    public StaticArray5(final Class<T> type, final T... values) {
        super(type, 5, values);
    }
}
