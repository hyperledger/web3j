package org.web3j.crypto;

import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionEncoderTest {

    @Test
    public void testSignMessage() {
        byte[] signedMessage = TransactionEncoder.signMessage(
                createEtherTransaction(), SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);
        assertThat(hexMessage,
                is("0xf85580010a840add5355887fffffffffffffff80"
                        + "1c"
                        + "a046360b50498ddf5566551ce1ce69c46c565f1f478bb0ee680caf31fbc08ab727"
                        + "a01b2f1432de16d110407d544f519fc91b84c8e16d3b6ec899592d486a94974cd0"));
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

    @Test
    public void testEip155Encode() {
        assertThat(TransactionEncoder.encode(createEip155RawTransaction(), (byte) 1),
                is(Numeric.hexStringToByteArray(
                        "0xec098504a817c800825208943535353535353535353535353535353535353535880de0"
                                + "b6b3a764000080018080")));
    }

    @Test
    public void testEip155Transaction() {
        // https://github.com/ethereum/EIPs/issues/155
        Credentials credentials = Credentials.create(
                "0x4646464646464646464646464646464646464646464646464646464646464646");

        assertThat(TransactionEncoder.signMessage(
                createEip155RawTransaction(), (byte) 1, credentials),
                is(Numeric.hexStringToByteArray(
                        "0xf86c098504a817c800825208943535353535353535353535353535353535353535880"
                                + "de0b6b3a76400008025a028ef61340bd939bc2195fe537567866003e1a15d"
                                + "3c71ff63e1590620aa636276a067cbe9d8997f761aecb703304b3800ccf55"
                                + "5c9f3dc64214b297fb1966a3b6d83")));
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

    public static RawTransaction createEip155RawTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(9), BigInteger.valueOf(20000000000L),
                BigInteger.valueOf(21000), "0x3535353535353535353535353535353535353535",
                BigInteger.valueOf(1000000000000000000L));
    }
}
