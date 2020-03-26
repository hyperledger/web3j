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
public class StaticArray25<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray25(final List<T> values) {
        super(25, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray25(final T... values) {
        super(25, values);
    }

    public StaticArray25(final Class<T> type, final List<T> values) {
        super(type, 25, values);
    }

    @SafeVarargs
    public StaticArray25(final Class<T> type, final T... values) {
        super(type, 25, values);
    }
}
