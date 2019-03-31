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
public class StaticArray829<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray829(List<T> values) {
        super(829, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray829(T... values) {
        super(829, values);
    }

    public StaticArray829(Class<T> type, List<T> values) {
        super(type, 829, values);
    }

    @SafeVarargs
    public StaticArray829(Class<T> type, T... values) {
        super(type, 829, values);
    }
}
