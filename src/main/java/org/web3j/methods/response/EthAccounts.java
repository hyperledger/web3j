package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

import java.util.List;

/**
 * eth_accounts
 */
public class EthAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
