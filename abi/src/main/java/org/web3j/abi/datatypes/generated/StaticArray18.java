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
public class StaticArray18<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray18(final List<T> values) {
        super(18, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray18(final T... values) {
        super(18, values);
    }

    public StaticArray18(final Class<T> type, final List<T> values) {
        super(type, 18, values);
    }

    @SafeVarargs
    public StaticArray18(final Class<T> type, final T... values) {
        super(type, 18, values);
    }
}
