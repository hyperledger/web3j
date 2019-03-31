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
public class StaticArray170<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray170(List<T> values) {
        super(170, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray170(T... values) {
        super(170, values);
    }

    public StaticArray170(Class<T> type, List<T> values) {
        super(type, 170, values);
    }

    @SafeVarargs
    public StaticArray170(Class<T> type, T... values) {
        super(type, 170, values);
    }
}
