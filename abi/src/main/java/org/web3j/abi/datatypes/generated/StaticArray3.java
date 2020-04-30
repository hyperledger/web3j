package org.web3j.abi.datatypes.generated;

import java.util.List;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray3<T extends Type<?>> extends StaticArray<T> {
    public StaticArray3(final Class<T> type, final List<T> values) {
        super(type, 3, values);
    }

    @SafeVarargs
    public StaticArray3(final Class<T> type, final T... values) {
        super(type, 3, values);
    }
}
