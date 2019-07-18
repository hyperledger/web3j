package org.web3j.protocol.eea.response;

import org.web3j.protocol.core.Response;

import java.util.List;

public class EeaFindPrivacyGroup extends Response<List<PrivacyGroup>> {
    public List<PrivacyGroup> getGroups() {
        return getResult();
    }
}
