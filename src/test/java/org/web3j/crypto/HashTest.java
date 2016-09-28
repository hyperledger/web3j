package org.web3j.crypto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.web3j.utils.Hex.b;

public class HashTest {

    @Test
    public void testSha3() {
        byte[] input = new byte[] {
                b(0x6, 0x8),
                b(0x6, 0x5),
                b(0x6, 0xc),
                b(0x6, 0xc),
                b(0x6, 0xf),
                b(0x2, 0x0),
                b(0x7, 0x7),
                b(0x6, 0xf),
                b(0x7, 0x2),
                b(0x6, 0xc),
                b(0x6, 0x4)
        };

        byte[] expected = new byte[]{
                b(0x4, 0x7),
                b(0x1, 0x7),
                b(0x3, 0x2),
                b(0x8, 0x5),
                b(0xa, 0x8),
                b(0xd, 0x7),
                b(0x3, 0x4),
                b(0x1, 0xe),
                b(0x5, 0xe),
                b(0x9, 0x7),
                b(0x2, 0xf),
                b(0xc, 0x6),
                b(0x7, 0x7),
                b(0x2, 0x8),
                b(0x6, 0x3),
                b(0x8, 0x4),
                b(0xf, 0x8),
                b(0x0, 0x2),
                b(0xf, 0x8),
                b(0xe, 0xf),
                b(0x4, 0x2),
                b(0xa, 0x5),
                b(0xe, 0xc),
                b(0x5, 0xf),
                b(0x0, 0x3),
                b(0xb, 0xb),
                b(0xf, 0xa),
                b(0x2, 0x5),
                b(0x4, 0xc),
                b(0xb, 0x0),
                b(0x1, 0xf),
                b(0xa, 0xd)
        };

        byte[] result = Hash.sha3(input);
        assertThat(result, is(expected));
    }

    @Test
    public void testSha3HashHex() {
        assertThat(Hash.sha3(""),
                is("0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470"));

        assertThat(Hash.sha3("68656c6c6f20776f726c64"),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testByte() {
        assertThat(b(0x0, 0x0), is((byte) 0x0));
        assertThat(b(0x1, 0x0), is((byte) 0x10));
        assertThat(b(0xf, 0xf), is((byte) 0xff));
        assertThat(b(0xc, 0x5), is((byte) 0xc5));
    }
}
