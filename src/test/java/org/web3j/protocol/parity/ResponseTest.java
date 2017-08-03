package org.web3j.protocol.parity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import org.web3j.protocol.ResponseTester;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.parity.methods.response.ParityAddressesResponse;
import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.web3j.protocol.parity.methods.response.ParityDeriveAddress;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityListRecentDapps;
import org.web3j.protocol.parity.methods.response.SignerRejectRequest;
import org.web3j.protocol.parity.methods.response.SignerRequestsToConfirm;


/**
 * Parity Protocol Response tests.
 */
public class ResponseTest extends ResponseTester {
    
    @Test
    public void testParityAddressesResponse() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": [\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]\n"
                + "}");

        ParityAddressesResponse parityAddressesResponse = deserialiseResponse(
                ParityAddressesResponse.class);
        assertThat(parityAddressesResponse.getAddresses(),equalTo(Arrays.asList(
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1"
                )));
    }
    
    @Test
    public void testParityAllAccountsInfo() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": {\n" 
                + "        \"0x00a289b43e1e4825dbedf2a78ba60a640634dc40\": {\n"
                + "            \"meta\": {},\n"
                + "            \"name\": \"Savings\",\n"
                + "            \"uuid\": \"7fee0393-7571-2b4f-8672-862fea01a4a0\""
                + "            }\n"
                + "    }\n"
                + "}");
        
        Map<String, ParityAllAccountsInfo.AccountsInfo> accountsInfoMap = new HashMap<>(1);
        accountsInfoMap.put("0x00a289b43e1e4825dbedf2a78ba60a640634dc40",
                new ParityAllAccountsInfo.AccountsInfo(
                        Collections.<String, Object>emptyMap(),
                        "Savings",
                        "7fee0393-7571-2b4f-8672-862fea01a4a0"                        
                ));

        ParityAllAccountsInfo parityAllAccountsInfo = deserialiseResponse(
                ParityAllAccountsInfo.class);
        assertThat(parityAllAccountsInfo.getAccountsInfo(), equalTo(accountsInfoMap));       
    }
    
    @Test
    public void testParityDefaultAddressResponse() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": \"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"\n"
                + "}");

        ParityDefaultAddressResponse parityDefaultAddressesResponse = deserialiseResponse(
                ParityDefaultAddressResponse.class);
        assertThat(parityDefaultAddressesResponse.getAddress(),is(
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1"));
    }
    
    @Test
    public void testParityDeriveAddress() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": \"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"\n"
                + "}");

        ParityDeriveAddress parityDeriveAddress = deserialiseResponse(
                ParityDeriveAddress.class);
        assertThat(parityDeriveAddress.getAddress(),is(
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1"));
    }   
    
    /*@Test
    public void testParityExportAccount() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": {\n"
                + "        \"address\": \"0042e5d2a662eeaca8a7e828c174f98f35d8925b\",\n"
                + "        \"crypto\": {\n" 
                + "            \"cipher\": \"aes-128-ctr\",\n"
                + "                \"cipherparams\": {\n"
                + "                    \"iv\": \"a1c6ff99070f8032ca1c4e8add006373\"\n"
                + "                },\n"
                + "            \"ciphertext\": \"df27e3db64aa18d984b6439443f73660643c2d119a6f0fa2fa9a6456fc802d75\",\n"
                + "            \"kdf\": \"pbkdf2\",\n"
                + "            \"kdfparams\": {\n"
                + "                \"c\": 10240,\n"
                + "                \"dklen\": 32,\n"
                + "                \"prf\": \"hmac-sha256\",\n"
                + "                \"salt\": \"ddc325335cda5567a1719313e73b4842511f3e4a837c9658eeb78e51ebe8c815\"\n"
                + "            },\n"
                + "        \"mac\": \"3dc888ae79cbb226ff9c455669f6cf2d79be72120f2298f6cb0d444fddc0aa3d\"\n"
                + "        },\n"
                + "    \"id\": \"6a186c80-7797-cff2-bc2e-7c1d6a6cc76e\",\n"
                + "    \"meta\": \"{\\\"passwordHint\\\":\\\"parity-export-test\\\",\\\"timestamp\\\":1490017814987}\",\n"
                + "    \"name\": \"parity-export-test\",\n"
                + "    \"version\": 3\n"
                + "    }\n"
                + "}");
        
        WalletFile walletFile = new WalletFile();
        walletFile.setAddress("0042e5d2a662eeaca8a7e828c174f98f35d8925b");

        WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher("aes-128-ctr");        
        crypto.setCiphertext("df27e3db64aa18d984b6439443f73660643c2d119a6f0fa2fa9a6456fc802d75");        
        walletFile.setCrypto(crypto);

        WalletFile.CipherParams cipherParams = new WalletFile.CipherParams();
        cipherParams.setIv("a1c6ff99070f8032ca1c4e8add006373");
        crypto.setCipherparams(cipherParams);

        crypto.setKdf("pbkdf2");
        WalletFile.Aes128CtrKdfParams kdfParams = new WalletFile.Aes128CtrKdfParams();
        kdfParams.setC(10240);
        kdfParams.setPrf("hmac-sha256");
        kdfParams.setDklen(32);
        kdfParams.setSalt("ddc325335cda5567a1719313e73b4842511f3e4a837c9658eeb78e51ebe8c815");
        crypto.setKdfparams(kdfParams);

        crypto.setMac("3dc888ae79cbb226ff9c455669f6cf2d79be72120f2298f6cb0d444fddc0aa3d");
        walletFile.setCrypto(crypto);
        walletFile.setId("6a186c80-7797-cff2-bc2e-7c1d6a6cc76e");
        walletFile.setVersion(3);       

        ParityExportAccount parityExportAccount = deserialiseResponse(
                ParityExportAccount.class);
        assertThat(parityExportAccount.getWallet(), equalTo(walletFile));
    }  */
    
    @Test
    public void testParityListRecentDapps() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": [\"web\"]\n"
                + "}");

        ParityListRecentDapps parityListRecentDapps = deserialiseResponse(
                ParityListRecentDapps.class);
        assertThat(parityListRecentDapps.getDappsIds(),equalTo(Arrays.asList(
                        "web"
                )));
    } 
    
    @Test
    public void testSignerRejectRequest() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\":true\n"
                + "}");

        SignerRejectRequest signerRejectRequest =
                deserialiseResponse(SignerRejectRequest.class);
        assertTrue(signerRejectRequest.isRejected());
    }
    
    @Test
    public void testPersonalRequestsToConfirm() {
        //CHECKSTYLE:OFF
        buildResponse("{\"jsonrpc\":\"2.0\",\"result\":["
                        + "{\"id\":\"0x1\",\"payload\":{\"transaction\":{\"data\":\"0x\",\"from\":\"0x0000000000000000000000000000000000000001\",\"gas\":\"0x989680\",\"gasPrice\":\"0x2710\",\"nonce\":null,\"to\":\"0xd46e8dd67c5d32be8058bb8eb970870f07244567\",\"value\":\"0x1\"}}},"
                        + "{\"id\":\"0x2\",\"payload\":{\"sign\":{\"address\":\"0x0000000000000000000000000000000000000001\",\"hash\":\"0x0000000000000000000000000000000000000000000000000000000000000005\"}}}"
                        + "],\"id\":1}");
        //CHECKSTYLE:ON

        List<SignerRequestsToConfirm.RequestsToConfirm> requestsToConfirm =
                Arrays.asList(new SignerRequestsToConfirm.RequestsToConfirm(
                                "0x1",
                                new SignerRequestsToConfirm.TransactionPayload(
                                        new SignerRequestsToConfirm.TransactionRequestType(
                                                "0x0000000000000000000000000000000000000001",
                                                "0xd46e8dd67c5d32be8058bb8eb970870f07244567",
                                                "0x989680",
                                                "0x2710",
                                                "0x1",
                                                "0x",
                                                null
                                        )
                                )
                            ),
                        new SignerRequestsToConfirm.RequestsToConfirm(
                                "0x2",
                                new SignerRequestsToConfirm.SignPayload(
                                        new SignerRequestsToConfirm.SignRequest(
                                                "0x0000000000000000000000000000000000000001",
                                                "0x0000000000000000000000000000000000000000"
                                                        + "000000000000000000000005"
                                        )
                                )
                        )
                );

        SignerRequestsToConfirm signerRequestsToConfirm =
                deserialiseResponse(SignerRequestsToConfirm.class);
        assertThat(signerRequestsToConfirm.getRequestsToConfirm(),equalTo(requestsToConfirm));
    }

    /*@Test
    public void testPersonalSignerEnabled() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": true\n"
                + "}");

        PersonalSignerEnabled personalSignerEnabled = deserialiseResponse(
                PersonalSignerEnabled.class);
        assertTrue(personalSignerEnabled.isSignerEnabled());
    }

    @Test
    public void testVoidResponse() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\":null\n"
                + "}");

        VoidResponse voidResponse = deserialiseResponse(VoidResponse.class);
        assertTrue(voidResponse.isValid());
    }

    @Test
    public void testPersonalAccountsInfo() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\":{\n"
                + "        \"0xfc390d8a8ddb591b010fda52f4db4945742c3809\":{\n"
                + "            \"name\":\"Savings\",\n"
                + "            \"uuid\":\"7fee0393-7571-2b4f-8672-862fea01a4a0\",\n"
                + "            \"meta\":{}\n"
                + "        }\n"
                + "    }\n"
                + "}");

        Map<String, ParityAllAccountsInfo.AccountsInfo> accountsInfoMap = new HashMap<>(1);
        accountsInfoMap.put("0xfc390d8a8ddb591b010fda52f4db4945742c3809",
                new ParityAllAccountsInfo.AccountsInfo(
                        "Savings",
                        "7fee0393-7571-2b4f-8672-862fea01a4a0",
                        Collections.<String, Object>emptyMap()
                ));

        ParityAllAccountsInfo personalAccountsInfo = deserialiseResponse(ParityAllAccountsInfo.class);
        assertThat(personalAccountsInfo.getAccountsInfo(), equalTo(accountsInfoMap));
    }
    
    */
}
