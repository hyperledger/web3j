package org.web3j.rlp;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RlpEncoderTest {

    /**
     * Examples taken from https://github.com/ethereum/wiki/wiki/RLP#examples
     */
    @Test
    public void testEncode() {
        assertThat(RlpEncoder.encode(new RlpString("dog")),
                is(new byte[]{ (byte) 0x83, 'd', 'o', 'g' }));

        assertThat(RlpEncoder.encode(new RlpList(new RlpString("cat"), new RlpString("dog"))),
                is(new byte[]{ (byte) 0xc8, (byte) 0x83, 'c', 'a', 't', (byte) 0x83, 'd', 'o', 'g'}));

        assertThat(RlpEncoder.encode(new RlpString("")),
                is(new byte[]{ (byte) 0x80 }));

        assertThat(RlpEncoder.encode(new RlpList()),
                is(new byte[]{ (byte) 0xc0 }));

        assertThat(RlpEncoder.encode(new RlpString("0x0f")),
                is(new byte[]{ (byte) 0x0f }));

        assertThat(RlpEncoder.encode(new RlpString("0x0400")),
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
                new RlpString("Lorem ipsum dolor sit amet, consectetur adipisicing elit")),
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
    }
}
