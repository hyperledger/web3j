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
public class StaticArray19<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray19(final List<T> values) {
        super(19, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray19(final T... values) {
        super(19, values);
    }

    public StaticArray19(final Class<T> type, final List<T> values) {
        super(type, 19, values);
    }

    @SafeVarargs
    public StaticArray19(final Class<T> type, final T... values) {
        super(type, 19, values);
    }
}
