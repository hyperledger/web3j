package org.web3j.protocol.core.generated;

import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.AbstractRemoteCall;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.TransactionManager;

public class RemoteCall8<T1, T2, T3, T4, T5, T6, T7, T8>
        extends AbstractRemoteCall<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

    public RemoteCall8(
            final Function function,
            final String contractAddress,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        super(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> convert(final List<Type<?>> values) {
        return new Tuple8<>(
                (T1) values.get(0).getValue(),
                (T2) values.get(1).getValue(),
                (T3) values.get(2).getValue(),
                (T4) values.get(3).getValue(),
                (T5) values.get(4).getValue(),
                (T6) values.get(5).getValue(),
                (T7) values.get(6).getValue(),
                (T8) values.get(7).getValue()
        );
    }
}
