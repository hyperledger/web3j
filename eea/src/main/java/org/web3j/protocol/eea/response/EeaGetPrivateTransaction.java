package org.web3j.protocol.eea.response;

import org.web3j.protocol.core.Response;

import java.util.Optional;

public class EeaGetPrivateTransaction extends Response<PrivateTransaction> {
    public Optional<PrivateTransaction> getPrivateTransaction() {
        return Optional.ofNullable(getResult());
    }
}
