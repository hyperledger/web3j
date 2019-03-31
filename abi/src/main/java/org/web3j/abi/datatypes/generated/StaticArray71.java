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
public class StaticArray71<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray71(List<T> values) {
        super(71, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray71(T... values) {
        super(71, values);
    }

    public StaticArray71(Class<T> type, List<T> values) {
        super(type, 71, values);
    }

    @SafeVarargs
    public StaticArray71(Class<T> type, T... values) {
        super(type, 71, values);
    }
}
