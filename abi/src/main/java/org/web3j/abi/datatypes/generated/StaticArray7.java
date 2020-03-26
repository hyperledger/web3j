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
public class StaticArray7<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray7(final List<T> values) {
        super(7, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray7(final T... values) {
        super(7, values);
    }

    public StaticArray7(final Class<T> type, final List<T> values) {
        super(type, 7, values);
    }

    @SafeVarargs
    public StaticArray7(final Class<T> type, final T... values) {
        super(type, 7, values);
    }
}
