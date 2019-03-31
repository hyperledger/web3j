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
public class StaticArray161<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray161(List<T> values) {
        super(161, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray161(T... values) {
        super(161, values);
    }

    public StaticArray161(Class<T> type, List<T> values) {
        super(type, 161, values);
    }

    @SafeVarargs
    public StaticArray161(Class<T> type, T... values) {
        super(type, 161, values);
    }
}
