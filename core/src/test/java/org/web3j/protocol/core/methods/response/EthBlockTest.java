package org.web3j.protocol.core.methods.response;

import org.junit.Test;
import org.web3j.utils.Numeric;

import static junit.framework.TestCase.assertEquals;

public class EthBlockTest {


    @Test
    public void testEthBlockNullSize()
    {
       EthBlock.Block ethBlock = new EthBlock.Block(null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null);


        assertEquals( ethBlock.getSize(), Numeric.decodeQuantity("0x0"));
    }
    @Test
    public void testEthBlockNotNullSize(){
        EthBlock.Block ethBlock = new EthBlock.Block(null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "0x3e8",
                null,
                null,
                null,
                null,
                null,
                null);

        assertEquals( ethBlock.getSize(), Numeric.decodeQuantity("0x3e8"));
    }

}
