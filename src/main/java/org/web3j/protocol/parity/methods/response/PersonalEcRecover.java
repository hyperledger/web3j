package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * Created by fanjl on 2017/4/10.
 */
public class PersonalEcRecover extends Response<String> {
    public String getRecoverAccountId() {
        return getResult();
    }
}
