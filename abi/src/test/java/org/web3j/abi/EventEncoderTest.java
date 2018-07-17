package org.web3j.abi;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class EventEncoderTest {

    private final String methodSignature;
    private final String expectedEncodedEvent;

    public EventEncoderTest(String methodSignature, String expectedEncodedEvent) {
        this.methodSignature = methodSignature;
        this.expectedEncodedEvent = expectedEncodedEvent;
    }

    @Test
    public void testBuildEventSignature() {
        assertThat(EventEncoder.buildEventSignature(methodSignature),
                is(expectedEncodedEvent));
    }

    @Parameterized.Parameters(
            name = "Test {index}: Given method {0} then should be encoded to {1}")
    public static List<Object[]> data() {
        return asList(
                new Object[] {
                    "Deposit(address,hash256,uint256)",
                    "0x50cb9fe53daa9737b786ab3646f04d0150dc50ef4e75f59509d83667ad5adb20"},
                new Object[] {
                    "Notify(uint256,uint256)",
                    "0x71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed"},
                new Object[] {
                    "LogFill(address,address,address,address,address,uint256,uint256,uint256,"
                        + "uint256,bytes32,bytes32)",
                    "0x0d0b9391970d9a25552f37d436d2aae2925e2bfe1b2a923754bada030c498cb3"},
                new Object[] {
                    "LogCancel(address,address,address,address,uint256,uint256,bytes32,bytes32)",
                    "0x67d66f160bc93d925d05dae1794c90d2d6d6688b29b84ff069398a9b04587131"},
                new Object[] {
                    "LogError(uint8,bytes32)",
                    "0x36d86c59e00bd73dc19ba3adfe068e4b64ac7e92be35546adeddf1b956a87e90"}
        );
    }
}
