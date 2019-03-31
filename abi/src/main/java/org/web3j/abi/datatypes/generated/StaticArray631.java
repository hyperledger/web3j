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
public class StaticArray631<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray631(List<T> values) {
        super(631, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray631(T... values) {
        super(631, values);
    }

    public StaticArray631(Class<T> type, List<T> values) {
        super(type, 631, values);
    }

    @SafeVarargs
    public StaticArray631(Class<T> type, T... values) {
        super(type, 631, values);
    }
}
