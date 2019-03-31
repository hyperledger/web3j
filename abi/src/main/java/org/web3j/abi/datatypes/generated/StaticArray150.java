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
public class StaticArray150<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray150(List<T> values) {
        super(150, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray150(T... values) {
        super(150, values);
    }

    public StaticArray150(Class<T> type, List<T> values) {
        super(type, 150, values);
    }

    @SafeVarargs
    public StaticArray150(Class<T> type, T... values) {
        super(type, 150, values);
    }
}
