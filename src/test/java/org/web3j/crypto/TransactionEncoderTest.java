package org.web3j.crypto;


import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Hex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionEncoderTest {

    @Test
    public void testSignMessage() {
        byte[] signedMessage = TransactionEncoder.signMessage(
                createEtherTransaction(), SampleKeys.KEY_PAIR);
        String hexMessage = Hex.toHexString(signedMessage);
        assertThat(hexMessage,
                is("0xf85580010a840add5355887fffffffffffffff80" +
                        "1c" +
                        "a0b782531e6ed4ae8f066f1ed9ec468c775a1a7b6f4f52d257eb89f16d133e4952" +
                        "a048a0c1403a88c087a1b9dd73143eb4de5c565b17a8151e1d06118495ec75eab9"));
    }

    @Test
    public void testEtherTransactionAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(createEtherTransaction(),
                new Sign.SignatureData((byte) 0, new byte[32], new byte[32]));
        assertThat(rlpStrings.size(), is(9));
        assertThat(rlpStrings.get(3), equalTo(RlpString.create(new BigInteger("add5355", 16))));
    }

    @Test
    public void testContractAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(
                createContractTransaction(), null);
        assertThat(rlpStrings.size(), is(6));
        assertThat(rlpStrings.get(3), is(RlpString.create("")));
    }

    private static Transaction createEtherTransaction() {
        return Transaction.createEtherTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, "0xadd5355",
                BigInteger.valueOf(Long.MAX_VALUE));
    }

    private static Transaction createContractTransaction() {
        return Transaction.createContractTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, BigInteger.valueOf(Long.MAX_VALUE),
                "01234566789");
    }
}
