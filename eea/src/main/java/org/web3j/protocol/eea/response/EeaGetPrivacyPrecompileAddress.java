package org.web3j.protocol.eea.response;

import org.web3j.protocol.core.Response;

public class EeaGetPrivacyPrecompileAddress extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
