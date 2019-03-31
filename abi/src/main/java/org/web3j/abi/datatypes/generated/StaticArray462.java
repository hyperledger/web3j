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
public class StaticArray462<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray462(List<T> values) {
        super(462, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray462(T... values) {
        super(462, values);
    }

    public StaticArray462(Class<T> type, List<T> values) {
        super(type, 462, values);
    }

    @SafeVarargs
    public StaticArray462(Class<T> type, T... values) {
        super(type, 462, values);
    }
}
