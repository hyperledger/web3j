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
public class StaticArray30<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray30(final List<T> values) {
        super(30, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray30(final T... values) {
        super(30, values);
    }

    public StaticArray30(final Class<T> type, final List<T> values) {
        super(type, 30, values);
    }

    @SafeVarargs
    public StaticArray30(final Class<T> type, final T... values) {
        super(type, 30, values);
    }
}
