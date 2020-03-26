package org.web3j.protocol.core.generated;

import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.TransactionManager;

public class RemoteFunctionCall4<T1, T2, T3, T4> extends RemoteFunctionCall<Tuple4<T1, T2, T3, T4>> {

    public RemoteFunctionCall4(
            final Function function,
            final String contractAddress,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        super(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Tuple4<T1, T2, T3, T4> convert(final List<Type<?>> values) {
        return new Tuple4<>(
                (T1) values.get(0),
                (T2) values.get(1),
                (T3) values.get(2),
                (T4) values.get(3)
        );
    }
}
