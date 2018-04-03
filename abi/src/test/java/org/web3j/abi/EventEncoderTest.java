package org.web3j.abi;

import junit.framework.Assert;
import org.junit.Test;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Int128;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventEncoderTest {

    @Test
    public void testEncode(){

        // arrange
        Event sampleEvent = new Event("Deposit",
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));

        // act
        String sampleEncodedString = EventEncoder.encode(sampleEvent);

        // assert
        Assert.assertEquals("0xc825f9469b7ee584b5ca44fe9b52926fadaf3ec8bfefd27be19dbdaad72ed229",sampleEncodedString);
    }

    @Test
    public void testBuildMethodSignature(){

        // arrange
        Event sampleEvent = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Int128>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}));

        // act
        String sampleMethodSignature = EventEncoder.buildMethodSignature(sampleEvent.getName(), sampleEvent.getNonIndexedParameters(), sampleEvent.getIndexedParameters());

        // assert
        Assert.assertEquals("Transfer(uint128,int128)",sampleMethodSignature);
    }

    @Test
    public void testBuildEventSignature() {
        assertThat(EventEncoder.buildEventSignature("Deposit(address,hash256,uint256)"),
                is("0x50cb9fe53daa9737b786ab3646f04d0150dc50ef4e75f59509d83667ad5adb20"));

        assertThat(EventEncoder.buildEventSignature("Notify(uint256,uint256)"),
                is("0x71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed"));
    }

}
