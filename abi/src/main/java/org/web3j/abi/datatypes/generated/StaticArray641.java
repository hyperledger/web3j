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
public class StaticArray641<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray641(List<T> values) {
        super(641, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray641(T... values) {
        super(641, values);
    }

    public StaticArray641(Class<T> type, List<T> values) {
        super(type, 641, values);
    }

    @SafeVarargs
    public StaticArray641(Class<T> type, T... values) {
        super(type, 641, values);
    }
}
