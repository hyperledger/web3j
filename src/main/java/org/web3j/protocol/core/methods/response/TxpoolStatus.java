package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * net_peerCount
 */
public class TxpoolStatus
{
    private String pending;
    private String queued;

    //public String getPending()
    //{
    //    return pending;
    //}

    public void setPending(String pending)
    {
        this.pending = pending;
    }

    //public String getQueued2()
    //{
    //    return queued;
    //}

    public void setQueued(String queued)
    {
        this.queued = queued;
    }

    public BigInteger getQueued()
    {
        return Numeric.decodeQuantity(queued);
    }

    public BigInteger getPending()
    {
        return Numeric.decodeQuantity(pending);
    }

}
