package org.conor10.web3j.methods;

import java.util.List;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_accounts
 */
public class EthAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
