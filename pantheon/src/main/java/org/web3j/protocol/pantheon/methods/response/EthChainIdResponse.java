package org.web3j.protocol.pantheon.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

public class EthChainIdResponse extends Response<String> {

    public Integer getChainId() {
        return Numeric.decodeQuantity(getResult()).intValue();
    }

}
