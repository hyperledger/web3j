package org.web3j.abi;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FunctionReturnDecoderTest {

    @Test
    public void testSimpleFunctionDecode() {
        Function function = new Function<>(
                "test",
                Collections.<Type>emptyList(),
                Collections.singletonList(Uint.class)
        );

        assertThat(FunctionReturnDecoder.decode(
                "0x0000000000000000000000000000000000000000000000000000000000000037",
                function.getOutputParameters()),
                equalTo(Collections.singletonList(new Uint(BigInteger.valueOf(55)))));
    }

    @Test
    public void testMultipleResultFunctionDecode() {
        Function function = new Function<>(
                "test",
                Collections.<Type>emptyList(),
                Arrays.asList(Uint.class, Uint.class)
        );

        assertThat(FunctionReturnDecoder.decode(
                "0x0000000000000000000000000000000000000000000000000000000000000037" +
                "0000000000000000000000000000000000000000000000000000000000000007",
                function.getOutputParameters()),
                equalTo(Arrays.asList(new Uint(BigInteger.valueOf(55)),
                        new Uint(BigInteger.valueOf(7)))));
    }
}
