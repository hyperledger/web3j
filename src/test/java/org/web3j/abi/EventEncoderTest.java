package org.web3j.abi;


import org.junit.Test;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Uint64;

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

    @Test
    public void testGetTypeName() {
        assertThat(EventEncoder.getTypeName(Uint.class), is("uint256"));
        assertThat(EventEncoder.getTypeName(Int.class), is("int256"));
        assertThat(EventEncoder.getTypeName(Ufixed.class), is("ufixed256"));
        assertThat(EventEncoder.getTypeName(Fixed.class), is("fixed256"));

        assertThat(EventEncoder.getTypeName(Uint64.class), is("uint64"));
        assertThat(EventEncoder.getTypeName(Int64.class), is("int64"));
        assertThat(EventEncoder.getTypeName(Bool.class), is("bool"));
        assertThat(EventEncoder.getTypeName(Utf8String.class), is("string"));
        assertThat(EventEncoder.getTypeName(DynamicBytes.class), is("bytes"));
    }
}
