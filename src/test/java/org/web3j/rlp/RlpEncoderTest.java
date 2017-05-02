package org.web3j.rlp;


import java.math.BigInteger;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RlpEncoderTest {

    /**
     * Examples taken from https://github.com/ethereum/wiki/wiki/RLP#examples.
     *
     * For further examples see https://github.com/ethereum/tests/tree/develop/RLPTests.
     */
    @Test
    public void testEncode() {
        assertThat(RlpEncoder.encode(RlpString.create("dog")),
                is(new byte[]{ (byte) 0x83, 'd', 'o', 'g' }));

        assertThat(RlpEncoder.encode(new RlpList(RlpString.create("cat"), RlpString.create("dog"))),
                is(new byte[]{ (byte) 0xc8, (byte) 0x83, 'c', 'a', 't', (byte) 0x83, 'd', 'o', 'g'}));

        assertThat(RlpEncoder.encode(RlpString.create("")),
                is(new byte[]{ (byte) 0x80 }));

        assertThat(RlpEncoder.encode(RlpString.create(new byte[] {})),
                is(new byte[]{ (byte) 0x80 }));

        assertThat(RlpEncoder.encode(RlpString.create(new byte[]{ 0, 'a', 0 })),
                is(new byte[]{ (byte) 0x82, 'a', 0 }));

        assertThat(RlpEncoder.encode(new RlpList()),
                is(new byte[]{ (byte) 0xc0 }));

        assertThat(RlpEncoder.encode(RlpString.create(BigInteger.valueOf(0x0f))),
                is(new byte[]{ (byte) 0x0f }));

        assertThat(RlpEncoder.encode(RlpString.create(BigInteger.valueOf(0x0400))),
                is(new byte[]{ (byte) 0x82, (byte) 0x04, (byte) 0x00 }));

        assertThat(RlpEncoder.encode(new RlpList(
                new RlpList(),
                new RlpList(new RlpList()),
                new RlpList(new RlpList(), new RlpList(new RlpList())))),
                is(new byte[] {
                        (byte) 0xc7,
                        (byte) 0xc0,
                        (byte) 0xc1, (byte) 0xc0,
                        (byte) 0xc3, (byte) 0xc0, (byte) 0xc1, (byte) 0xc0 }));

        assertThat(RlpEncoder.encode(
                RlpString.create("Lorem ipsum dolor sit amet, consectetur adipisicing elit")),
                is(new byte[] {
                        (byte) 0xb8,
                        (byte) 0x38,
                        'L', 'o', 'r', 'e', 'm', ' ', 'i', 'p', 's', 'u', 'm', ' ',
                        'd', 'o', 'l', 'o', 'r', ' ', 's', 'i', 't', ' ',
                        'a', 'm', 'e', 't', ',', ' ',
                        'c', 'o', 'n', 's', 'e', 'c', 't', 'e', 't', 'u', 'r', ' ',
                        'a', 'd', 'i', 'p', 'i', 's', 'i', 'c', 'i', 'n', 'g', ' ',
                        'e', 'l', 'i', 't'
                }));

        assertThat(RlpEncoder.encode(RlpString.create(BigInteger.ZERO)),
                is(new byte[]{ (byte) 0x80 }));

        assertThat(RlpEncoder.encode(RlpString.create(new byte[] { 0 })),
                is(new byte[]{ (byte) 0x00 }));
    }
}
