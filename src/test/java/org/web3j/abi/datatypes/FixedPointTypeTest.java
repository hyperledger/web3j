package org.web3j.abi.datatypes;

import java.math.BigInteger;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FixedPointTypeTest {

    @Test
    public void testConvert() {
        assertThat(
                FixedPointType.convert(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                is(new BigInteger("220000000000000000000000000000000", 16))
        );

        assertThat(
                FixedPointType.convert(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8)),
                is(new BigInteger("880000000000000000000000000000000", 16))
        );

        assertThat(
                FixedPointType.convert(BigInteger.valueOf(0xAAFF), BigInteger.valueOf(0x1111)),
                is(new BigInteger("AAFF11110000000000000000000000000000", 16))
        );
    }
}
