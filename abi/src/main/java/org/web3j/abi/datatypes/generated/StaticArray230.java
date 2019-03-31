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
public class StaticArray230<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray230(List<T> values) {
        super(230, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray230(T... values) {
        super(230, values);
    }

    public StaticArray230(Class<T> type, List<T> values) {
        super(type, 230, values);
    }

    @SafeVarargs
    public StaticArray230(Class<T> type, T... values) {
        super(type, 230, values);
    }
}
