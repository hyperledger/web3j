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
public class StaticArray480<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray480(List<T> values) {
        super(480, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray480(T... values) {
        super(480, values);
    }

    public StaticArray480(Class<T> type, List<T> values) {
        super(type, 480, values);
    }

    @SafeVarargs
    public StaticArray480(Class<T> type, T... values) {
        super(type, 480, values);
    }
}
