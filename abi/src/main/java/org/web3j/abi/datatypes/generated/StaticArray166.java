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
public class StaticArray166<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray166(List<T> values) {
        super(166, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray166(T... values) {
        super(166, values);
    }

    public StaticArray166(Class<T> type, List<T> values) {
        super(type, 166, values);
    }

    @SafeVarargs
    public StaticArray166(Class<T> type, T... values) {
        super(type, 166, values);
    }
}
