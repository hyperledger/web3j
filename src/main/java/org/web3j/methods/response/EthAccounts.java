package org.web3j.methods.response;

import java.util.List;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_accounts
 */
public class EthAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
