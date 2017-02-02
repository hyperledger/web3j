package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.crypto.Hash;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Sign transaction using Ethereum node.
 */
public class SignTransactionIT extends Scenario {

    @Test
    public void testSignTransaction() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        RawTransaction rawTransaction = createTransaction();

        byte[] encoded = TransactionEncoder.encode(rawTransaction);
        byte[] hashed = Hash.sha3(encoded);

        EthSign ethSign = parity.ethSign(ALICE.getAddress(), Numeric.toHexString(hashed)).sendAsync().get();

        String signature = ethSign.getSignature();
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    private static RawTransaction createTransaction() {
        BigInteger value = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();

        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(1048587), BigInteger.valueOf(500000), BigInteger.valueOf(500000),
                "0x9C98E381Edc5Fe1Ac514935F3Cc3eDAA764cf004",
                value);
    }
}
