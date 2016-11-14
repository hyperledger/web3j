package org.web3j.crypto;


import java.math.BigInteger;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RawTransactionEncoderTest {

    @Test
    public void testSignMessage() {
        byte[] signedMessage = TransactionEncoder.signMessage(
                createEtherTransaction(), SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);
        assertThat(hexMessage,
                is("0xf85580010a840add5355887fffffffffffffff80" +
                        "1c" +
                        "a046360b50498ddf5566551ce1ce69c46c565f1f478bb0ee680caf31fbc08ab727" +
                        "a01b2f1432de16d110407d544f519fc91b84c8e16d3b6ec899592d486a94974cd0"));
    }

    @Test
    public void testEtherTransactionAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(createEtherTransaction(),
                new Sign.SignatureData((byte) 0, new byte[32], new byte[32]));
        assertThat(rlpStrings.size(), is(9));
        assertThat(rlpStrings.get(3),
                IsEqual.<RlpType>equalTo(RlpString.create(new BigInteger("add5355", 16))));
    }

    @Test
    public void testContractAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(
                createContractTransaction(), null);
        assertThat(rlpStrings.size(), is(6));
        assertThat(rlpStrings.get(3),
                CoreMatchers.<RlpType>is(RlpString.create("")));
    }

    private static RawTransaction createEtherTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, "0xadd5355",
                BigInteger.valueOf(Long.MAX_VALUE));
    }

    private static RawTransaction createContractTransaction() {
        return RawTransaction.createContractTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, BigInteger.valueOf(Long.MAX_VALUE),
                "01234566789");
    }
}
