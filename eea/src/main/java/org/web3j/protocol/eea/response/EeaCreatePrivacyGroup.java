package org.web3j.protocol.eea.response;

import org.web3j.protocol.core.Response;

public class EeaCreatePrivacyGroup extends Response<String> {
    public String getPrivacyGroupId() {
        return getResult();
    }
}
