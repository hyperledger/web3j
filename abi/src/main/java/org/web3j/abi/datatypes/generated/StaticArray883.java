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
public class StaticArray883<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray883(List<T> values) {
        super(883, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray883(T... values) {
        super(883, values);
    }

    public StaticArray883(Class<T> type, List<T> values) {
        super(type, 883, values);
    }

    @SafeVarargs
    public StaticArray883(Class<T> type, T... values) {
        super(type, 883, values);
    }
}
