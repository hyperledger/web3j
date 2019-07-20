package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.eea.Eea;

/**
 * PrivateTransactionManager implementation for using a Pantheon node to transact.
 */
public class EeaTransactionManagerPantheon extends PrivateTransactionManager {

    private String privacyGroupId;

    public EeaTransactionManagerPantheon(
            final Eea eea,
            final Credentials credentials,
            final long chainId,
            final String privateFrom,
            final String privacyGroupId,
            final int attempts,
            final int sleepDuration) {
        super(eea, credentials, chainId, privateFrom, attempts, sleepDuration);
        this.privacyGroupId = privacyGroupId;
    }

    public EeaTransactionManagerPantheon(
            final Eea eea, final Credentials credentials, final long chainId,
            final String privateFrom, final String privacyGroupId) {
        this(eea, credentials, chainId, privateFrom, privacyGroupId,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, 15 * 1000);
    }

    @Override
    public String getPrivacyGroupId() {
        return privacyGroupId;
    }

    @Override
    Object privacyGroupIdOrPrivateFor() {
        return getPrivacyGroupId();
    }
}
