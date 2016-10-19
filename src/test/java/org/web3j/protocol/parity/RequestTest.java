package org.web3j.protocol.parity;


import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.methods.request.Wallet;


public class RequestTest extends RequestTester {

    private Parity web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Parity.build(httpService);
    }

    @Test
    public void testPersonalSignerEnabled() throws Exception {
        web3j.personalSignerEnabled().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_signerEnabled\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testPersonalListAccounts() throws Exception {
        web3j.personalListAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_listAccounts\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testPersonalNewAccount() throws Exception {
        web3j.personalNewAccount("password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccount\",\"params\":[\"password\"],\"id\":1}");
    }

    @Test
    public void testPersonalNewAccountFromPhrase() throws Exception {
        web3j.personalNewAccountFromPhrase("phrase", "password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccountFromPhrase\",\"params\":[\"phrase\",\"password\"],\"id\":1}");
    }

    @Test
    public void testPersonalNewAccountFromWallet() throws Exception {
        web3j.personalNewAccountFromWallet(
                new Wallet(
                        "0x12345",
                        new Wallet.Crypto(
                                "CIPHER",
                                "CIPHERTEXT",
                                Collections.<String, String>emptyMap(),
                                "KDF",
                                Collections.<String, String>emptyMap(),
                                "MAC"
                        ),
                        "0x1"
                ), "password"
        ).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccountFromWallet\",\"params\":[{\"address\":\"0x12345\",\"crypto\":{\"cipher\":\"CIPHER\",\"ciphertext\":\"CIPHERTEXT\",\"cipherparams\":{},\"kdf\":\"KDF\",\"kdfparams\":{},\"mac\":\"MAC\"},\"id\":\"0x1\"},\"password\"],\"id\":1}");
    }

    @Test
    public void testPersonalUnlockAccount() throws Exception {
        web3j.personalUnlockAccount("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "hunter2", BigInteger.ONE).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_unlockAccount\",\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"hunter2\",\"0x1\"],\"id\":1}");
    }

    @Test
    public void testPersonalUnlockAccountNoDuration() throws Exception {
        web3j.personalUnlockAccount("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "hunter2").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_unlockAccount\",\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"hunter2\",null],\"id\":1}");
    }

    @Test
    public void testPersonalSignAndSendTransaction() throws Exception {
        web3j.personalSignAndSendTransaction(
                new Transaction(
                        "FROM",
                        BigInteger.ONE,
                        BigInteger.TEN,
                        BigInteger.ONE,
                        "TO",
                        BigInteger.ZERO,
                        "DATA"
                ),
                "password"
        ).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_signAndSendTransaction\",\"params\":[{\"from\":\"FROM\",\"to\":\"TO\",\"gas\":\"0x1\",\"gasPrice\":\"0xa\",\"value\":\"0x0\",\"data\":\"DATA\",\"nonce\":\"0x1\"},\"password\"],\"id\":1}");
    }

    @Test
    public void testPersonalSetAccountName() throws Exception {
        web3j.personalSetAccountName("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "Savings").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_setAccountName\",\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"Savings\"],\"id\":1}");
    }

    @Test
    public void testPersonalSetAccountMeta() throws Exception {
        Map<String, Object> meta = new HashMap<>(1);
        meta.put("foo", "bar");
        web3j.personalSetAccountMeta("0xfc390d8a8ddb591b010fda52f4db4945742c3809", meta).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_setAccountMeta\",\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",{\"foo\":\"bar\"}],\"id\":1}");
    }

    @Test
    public void testPersonalAccountsInfo() throws Exception {
        web3j.personalAccountsInfo().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_accountsInfo\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testPersonalRequestsToConfirm() throws Exception {
        web3j.personalRequestsToConfirm().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_requestsToConfirm\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testPersonalConfirmRequest() throws Exception {
        web3j.personalConfirmRequest(
                "0x1",
                Transaction.createEthCallTransaction("0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc", "0x0"),
                "password"
        ).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_confirmRequest\",\"params\":[\"0x1\",{\"to\":\"0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc\",\"data\":\"0x0\"},\"password\"],\"id\":1}");
    }

    @Test
    public void testPersonalRejectRequest() throws Exception {
        web3j.personalRejectRequest("0x1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_rejectRequest\",\"params\":[\"0x1\"],\"id\":1}");
    }
}
