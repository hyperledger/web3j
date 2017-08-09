package org.web3j.protocol.parity.methods.response;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.Response;

/**
 * parity_ExportAccount.
 */
public class ParityExportAccount extends Response<WalletFile> {
    public WalletFile getWallet() {
        return getResult();
    }
}
