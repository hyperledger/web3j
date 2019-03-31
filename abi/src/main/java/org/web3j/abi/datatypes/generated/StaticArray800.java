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
public class StaticArray800<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray800(List<T> values) {
        super(800, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray800(T... values) {
        super(800, values);
    }

    public StaticArray800(Class<T> type, List<T> values) {
        super(type, 800, values);
    }

    @SafeVarargs
    public StaticArray800(Class<T> type, T... values) {
        super(type, 800, values);
    }
}
