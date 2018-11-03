package org.web3j.protocol.pantheon.response;

import org.web3j.protocol.core.Response;

import java.util.Map;

public class PantheonEthAccountsMap extends Response<Map<String, Boolean>> {
    public Map<String, Boolean> getAccounts() {
        return getResult();
    }
}
