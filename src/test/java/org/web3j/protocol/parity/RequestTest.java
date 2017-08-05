package org.web3j.protocol.parity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;

public class RequestTest extends RequestTester {

    private Parity web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Parity.build(httpService);
    }
    
    @Test
    public void testParityAllAccountsInfo() throws Exception {
        web3j.parityAllAccountsInfo().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_allAccountsInfo\","
                + "\"params\":[],\"id\":1}");
    }
    
    @Test
    public void testParityChangePassword() throws Exception {
        //CHECKSTYLE:OFF
        web3j.parityChangePassword("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2", "bazqux5").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_changePassword\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\",\"bazqux5\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityDeriveAddressHash() throws Exception {
        Map<String, Object> hashType = new LinkedHashMap<>(2);
        hashType.put("hash","0x2547ea3382099c7c76d33dd468063b32d41016aacb02cbd51ebc14ff5d2b6a43");
        hashType.put("type","hard");                
        
        web3j.parityDeriveAddressHash("0x407d73d8a49eeb85d32cf465507dd71d507100c1", 
                "hunter2", hashType, false).send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_deriveAddressHash\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\","
                + "{\"hash\":\"0x2547ea3382099c7c76d33dd468063b32d41016aacb02cbd51ebc14ff5d2b6a43\",\"type\":\"hard\"},false],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityDeriveAddressIndex() throws Exception {
        Map<String, Object> firstIndex = new LinkedHashMap<>(2);
        firstIndex.put("index",1);
        firstIndex.put("type","hard");                
        Map<String, Object> secondIndex = new LinkedHashMap<>(2);
        secondIndex.put("index",2);
        secondIndex.put("type","soft");                
        List<Map<String, Object>> indexType = new ArrayList<>();
        indexType.add(firstIndex);
        indexType.add(secondIndex);
        
        //CHECKSTYLE:OFF
        web3j.parityDeriveAddressIndex("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2", indexType, false).send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_deriveAddressIndex\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\","
                + "[{\"index\":1,\"type\":\"hard\"},{\"index\":2,\"type\":\"soft\"}],false],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityExportAccount() throws Exception {
        web3j.parityExportAccount("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2").send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_exportAccount\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityGetDappAddresses() throws Exception {
        web3j.parityGetDappAddresses("web").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getDappAddresses\","
                + "\"params\":[\"web\"],\"id\":1}");
    }
    
    @Test
    public void testParityGetDefaultDappAddress() throws Exception {
        web3j.parityGetDappDefaultAddress("web").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getDappDefaultAddress\","
                + "\"params\":[\"web\"],\"id\":1}");
    }
    
    public void testParityGetNewDappsAddresses() throws Exception {
        web3j.parityAllAccountsInfo().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getNewDappsAddresses\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityGetNewDappsDefaultAddress() throws Exception {
        web3j.parityGetNewDappsDefaultAddress().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getNewDappsDefaultAddress\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityImportGethAccounts() throws Exception {
        ArrayList<String> gethAccounts = new ArrayList<>();
        gethAccounts.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        web3j.parityImportGethAccounts(gethAccounts).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_importGethAccounts\","
                + "\"params\":[[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],\"id\":1}");
    }
    
    public void testParityKillAccount() throws Exception {
        web3j.parityKillAccount("0x407d73d8a49eeb85d32cf465507dd71d507100c1","hunter2").send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_killAccount\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    public void testParityListGethAccounts() throws Exception {
        web3j.parityListGethAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listGethAccounts\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityListRecentDapps() throws Exception {
        web3j.parityListRecentDapps().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listRecentDapps\","
                + "\"params\":[],\"id\":1}");
    }
    
    @Test
    public void testParityNewAccountFromPhrase() throws Exception {
        web3j.parityNewAccountFromPhrase("phrase", "password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromPhrase\","
                + "\"params\":[\"phrase\",\"password\"],\"id\":1}");
    }
    
    @Test
    public void testParityNewAccountFromSecret() throws Exception {
        //CHECKSTYLE:OFF
        web3j.parityNewAccountFromSecret("0x1db2c0cf57505d0f4a3d589414f0a0025ca97421d2cd596a9486bc7e2cd2bf8b", "password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromSecret\","
                + "\"params\":[\"0x1db2c0cf57505d0f4a3d589414f0a0025ca97421d2cd596a9486bc7e2cd2bf8b\",\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityNewAccountFromWallet() throws Exception {
        WalletFile walletFile = new WalletFile();
        walletFile.setAddress("0x...");

        WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher("CIPHER");
        crypto.setCiphertext("CIPHERTEXT");
        walletFile.setCrypto(crypto);

        WalletFile.CipherParams cipherParams = new WalletFile.CipherParams();
        cipherParams.setIv("IV");
        crypto.setCipherparams(cipherParams);

        crypto.setKdf("KDF");
        WalletFile.ScryptKdfParams kdfParams = new WalletFile.ScryptKdfParams();
        kdfParams.setDklen(32);
        kdfParams.setN(1);
        kdfParams.setP(10);
        kdfParams.setR(100);
        kdfParams.setSalt("SALT");
        crypto.setKdfparams(kdfParams);

        crypto.setMac("MAC");
        walletFile.setCrypto(crypto);
        walletFile.setId("cab06c9e-79a9-48ea-afc7-d3bdb3a59526");
        walletFile.setVersion(1);

        web3j.parityNewAccountFromWallet(walletFile, "password").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromWallet\",\"params\":[{\"address\":\"0x...\",\"id\":\"cab06c9e-79a9-48ea-afc7-d3bdb3a59526\",\"version\":1,\"crypto\":{\"cipher\":\"CIPHER\",\"ciphertext\":\"CIPHERTEXT\",\"cipherparams\":{\"iv\":\"IV\"},\"kdf\":\"KDF\",\"kdfparams\":{\"dklen\":32,\"n\":1,\"p\":10,\"r\":100,\"salt\":\"SALT\"},\"mac\":\"MAC\"}},\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    public void testParityRemoveAddress() throws Exception {
        web3j.parityRemoveAddress("0x407d73d8a49eeb85d32cf465507dd71d507100c1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_removeAddress\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],\"id\":1}");
    }
    
    @Test
    public void testParitySetAccountMeta() throws Exception {
        Map<String, Object> meta = new HashMap<>(1);
        meta.put("foo", "bar");
        web3j.paritySetAccountMeta("0xfc390d8a8ddb591b010fda52f4db4945742c3809", meta).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setAccountMeta\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",{\"foo\":\"bar\"}],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetAccountName() throws Exception {
        web3j.paritySetAccountName("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "Savings")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setAccountName\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"Savings\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetDappAddresses() throws Exception {
        ArrayList<String> dAppAddresses = new ArrayList<>();
        dAppAddresses.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        web3j.paritySetDappAddresses("web", dAppAddresses)
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setDappAddresses\","
                + "\"params\":[\"web\",[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetDappDefaultAddress() throws Exception {
        web3j.paritySetDappDefaultAddress("web", "0x407d73d8a49eeb85d32cf465507dd71d507100c1")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setDappDefaultAddress\","
                + "\"params\":[\"web\",\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetNewDappsAddresses() throws Exception {
        ArrayList<String> dAppAddresses = new ArrayList<>();
        dAppAddresses.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        web3j.paritySetNewDappsAddresses(dAppAddresses)
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setNewDappsAddresses\","
                + "\"params\":[[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetNewDappsDefaultAddress() throws Exception {
        web3j.paritySetNewDappsDefaultAddress("0x407d73d8a49eeb85d32cf465507dd71d507100c1")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setNewDappsDefaultAddress\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParityTestPassword() throws Exception {
        web3j.parityTestPassword("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_testPassword\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySignMessage() throws Exception {
        //CHECKSTYLE:OFF
        web3j.paritySignMessage("0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e","password1","0xbc36789e7a1e281436464229828f817d6612f7b477d66591ff96a9e064bcc98a").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_signMessage\","
                + "\"params\":[\"0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e\",\"password1\","
                + "\"0xbc36789e7a1e281436464229828f817d6612f7b477d66591ff96a9e064bcc98a\"],\"id\":1}");
        //CHECKSTYLE:ON
    }  
    
    @Test
    public void testSignerRequestsToConfirm() throws Exception {
        web3j.signerRequestsToConfirm().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"signer_requestsToConfirm\","
                + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testSignerConfirmRequest() throws Exception {
        web3j.signerConfirmRequest(
                "0x1",
                Transaction.createEthCallTransaction(
                        "0xa010fbad79f5e602699fff2bb4919fbd87abc8cc",
                        "0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc", "0x0"),
                "password"
        ).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"signer_confirmRequest\",\"params\":[\"0x1\",{\"from\":\"0xa010fbad79f5e602699fff2bb4919fbd87abc8cc\",\"to\":\"0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc\",\"data\":\"0x0\"},\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }       

    @Test
    public void testSignerRejectRequest() throws Exception {
        web3j.signerRejectRequest("0x1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"signer_rejectRequest\","
                + "\"params\":[\"0x1\"],\"id\":1}");
    } 
}