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
public class StaticArray640<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray640(List<T> values) {
        super(640, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray640(T... values) {
        super(640, values);
    }

    public StaticArray640(Class<T> type, List<T> values) {
        super(type, 640, values);
    }

    @SafeVarargs
    public StaticArray640(Class<T> type, T... values) {
        super(type, 640, values);
    }
}
