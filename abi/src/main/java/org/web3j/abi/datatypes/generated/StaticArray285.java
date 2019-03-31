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
public class StaticArray285<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray285(List<T> values) {
        super(285, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray285(T... values) {
        super(285, values);
    }

    public StaticArray285(Class<T> type, List<T> values) {
        super(type, 285, values);
    }

    @SafeVarargs
    public StaticArray285(Class<T> type, T... values) {
        super(type, 285, values);
    }
}
