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
public class StaticArray960<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray960(List<T> values) {
        super(960, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray960(T... values) {
        super(960, values);
    }

    public StaticArray960(Class<T> type, List<T> values) {
        super(type, 960, values);
    }

    @SafeVarargs
    public StaticArray960(Class<T> type, T... values) {
        super(type, 960, values);
    }
}
