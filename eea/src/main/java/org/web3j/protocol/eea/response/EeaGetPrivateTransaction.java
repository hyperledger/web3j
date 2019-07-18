package org.web3j.protocol.eea.response;

import java.util.Optional;

import org.web3j.protocol.core.Response;

public class EeaGetPrivateTransaction extends Response<PrivateTransaction> {
    public Optional<PrivateTransaction> getPrivateTransaction() {
        return Optional.ofNullable(getResult());
    }
}
