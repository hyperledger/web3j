package org.web3j.protocol.eea.response;

import java.util.List;

import org.web3j.protocol.core.Response;

public class EeaFindPrivacyGroup extends Response<List<PrivacyGroup>> {
    public List<PrivacyGroup> getGroups() {
        return getResult();
    }
}
