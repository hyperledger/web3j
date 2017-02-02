package org.web3j.abi;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventEncoderTest {

    @Test
    public void testBuildEventSignature() {
        assertThat(EventEncoder.buildEventSignature("Deposit(address,hash256,uint256)"),
                is("0x50cb9fe53daa9737b786ab3646f04d0150dc50ef4e75f59509d83667ad5adb20"));

        assertThat(EventEncoder.buildEventSignature("Notify(uint256,uint256)"),
                is("0x71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed"));
    }
}
