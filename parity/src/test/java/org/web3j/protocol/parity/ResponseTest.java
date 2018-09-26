package org.web3j.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.core.methods.response.VoidResponse;
import org.web3j.protocol.parity.methods.response.FullTraceInfo;
import org.web3j.protocol.parity.methods.response.ParityAddressesResponse;
import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.web3j.protocol.parity.methods.response.ParityDeriveAddress;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityFullTraceResponse;
import org.web3j.protocol.parity.methods.response.ParityListRecentDapps;
import org.web3j.protocol.parity.methods.response.StateDiff;
import org.web3j.protocol.parity.methods.response.Trace;
import org.web3j.protocol.parity.methods.response.VMTrace;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
    
    @Test
    public void testParityExportAccount() {
        //CHECKSTYLE:OFF
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
        //CHECKSTYLE:ON
        
        WalletFile walletFile = new WalletFile();
        walletFile.setAddress("0042e5d2a662eeaca8a7e828c174f98f35d8925b");

        WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher("aes-128-ctr");  
        //CHECKSTYLE:OFF
        crypto.setCiphertext("df27e3db64aa18d984b6439443f73660643c2d119a6f0fa2fa9a6456fc802d75");  
        //CHECKSTYLE:ON
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
    }
    
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
    public void testParityFullTraceResponseStateDiff() {
        //CHECKSTYLE:OFF
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\": {\n"
                + "        \"output\": \"0x\",\n"
                + "        \"stateDiff\": {\n"
                + "            \"0x00a0a24b9f0e5ec7aa4c7389b8302fd0123194de\": {\n"
                + "                \"balance\": {\n"
                + "                    \"*\": {\n"
                + "                        \"from\": \"0x2067ee238a4648bed5797\",\n"
                + "                        \"to\": \"0x2067ee23f5d09db3d0397\"\n"
                + "                    }\n"
                + "                },\n"
                + "                \"code\": \"=\",\n"
                + "                \"nonce\": \"=\",\n"
                + "                \"storage\": {}\n"
                + "            },\n"
                + "            \"0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a\": {\n"
                + "                \"balance\": {\n"
                + "                    \"*\": {\n"
                + "                        \"from\": \"0xf85a746b58c1fee\",\n"
                + "                        \"to\": \"0xf7eeea1663c73ee\"\n"
                + "                    }\n"
                + "                },\n"
                + "                \"code\": \"=\",\n"
                + "                \"nonce\": {\n"
                + "                    \"*\": {\n"
                + "                        \"from\": \"0x15\",\n"
                + "                        \"to\": \"0x16\"\n"
                + "                    }\n"
                + "                },\n"
                + "                \"storage\": {}\n"
                + "            },\n"
                + "            \"0x1a4298d0edc00618310e4c26f6479e5cccdfeaf8\": {\n"
                + "                \"balance\": {\n"
                + "                    \"+\": \"0x0\"\n"
                + "                },\n"
                + "                \"code\": {\n"
                + "                    \"+\": \"0x6060604052361561004a576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806341c0e1b51461004e578063b46300ec14610063575b5b5b005b341561005957600080fd5b61006161006d565b005b61006b6100ff565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614156100fc576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16ff5b5b565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc3073ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f19350505050151561017757600080fd5b5b5600a165627a7a72305820a6f301a38f55ea4c326a17bb26afad4aad7ed9dd49e1954d2b8995595e0ffceb0029\"\n"
                + "                },\n"
                + "                \"nonce\": {\n"
                + "                    \"+\": \"0x1\"\n"
                + "                },\n"
                + "                \"storage\": {\n"
                + "                    \"0x0000000000000000000000000000000000000000000000000000000000000000\": {\n"
                + "                        \"+\": \"0x00000000000000000000000014772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a\"\n"
                + "                    }\n"
                + "                }\n"
                + "            }\n"
                + "        }"
                + "    },\n"
                + "    \"id\": 1\n"
                + "}"
        );
        //CHECKSTYLE:ON

        ParityFullTraceResponse response = deserialiseResponse(ParityFullTraceResponse.class);
        assertNotNull(response);

        Map<String, StateDiff> stateDiffMap = new LinkedHashMap<>();
        stateDiffMap.put("0x00a0a24b9f0e5ec7aa4c7389b8302fd0123194de", new StateDiff(
                new StateDiff.ChangedState("0x2067ee238a4648bed5797", "0x2067ee23f5d09db3d0397"),
                new StateDiff.UnchangedState(),
                new StateDiff.UnchangedState(),
                new HashMap<>()
        ));
        stateDiffMap.put("0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a", new StateDiff(
                new StateDiff.ChangedState("0xf85a746b58c1fee", "0xf7eeea1663c73ee"),
                new StateDiff.UnchangedState(),
                new StateDiff.ChangedState("0x15", "0x16"),
                new HashMap<>()
        ));
        //CHECKSTYLE:OFF
        stateDiffMap.put("0x1a4298d0edc00618310e4c26f6479e5cccdfeaf8", new StateDiff(
                new StateDiff.AddedState("0x0"),
                new StateDiff.AddedState("0x6060604052361561004a576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806341c0e1b51461004e578063b46300ec14610063575b5b5b005b341561005957600080fd5b61006161006d565b005b61006b6100ff565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614156100fc576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16ff5b5b565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc3073ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f19350505050151561017757600080fd5b5b5600a165627a7a72305820a6f301a38f55ea4c326a17bb26afad4aad7ed9dd49e1954d2b8995595e0ffceb0029"),
                new StateDiff.AddedState("0x1"),
                new HashMap<>(Collections.singletonMap(
                        "0x0000000000000000000000000000000000000000000000000000000000000000",
                        new StateDiff.AddedState("0x00000000000000000000000014772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a"))
                )
        ));
        //CHECKSTYLE:ON
        FullTraceInfo info = new FullTraceInfo("0x", stateDiffMap, null, null);
        assertThat(response.getFullTraceInfo(), equalTo(info));
    }

    @Test
    public void testParityFullTraceResponseTraces() {
        //CHECKSTYLE:OFF
        // hacked together from multiple requests and the code shortened
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\": {\n"
                + "        \"output\": \"0x\",\n"
                + "        \"stateDiff\": null,\n"
                + "        \"trace\": [{\n"
                + "                \"action\": {\n"
                + "                    \"from\": \"0x6c24f4387b31251fd7b6d7a1269d880b2108bf3a\",\n"
                + "                    \"gas\": \"0x4bc1f5\",\n"
                + "                    \"init\": \"0x606060405234156200000d57fe5b60405162004e4038038062004e40833981016040908152815\",\n"
                + "                    \"value\": \"0x0\"\n"
                + "                },\n"
                + "                \"blockHash\": \"0xf831abfdc67fb2ea3d16b6510b8e37d3d3e52ce3c25ed345053cb9503a430dd8\",\n"
                + "                \"blockNumber\": 4019912,\n"
                + "                \"result\": {\n"
                + "                    \"address\": \"0x1c9997559f6f1ae2ece8525eab68709f50865165\",\n"
                + "                    \"code\": \"0x606060405236156102b95763ffffffff60e060020a600035041663095ea7b381146102bb578063\",\n"
                + "                    \"gasUsed\": \"0x3ef6fe\"\n"
                + "                },\n"
                + "                \"subtraces\": 0,\n"
                + "                \"traceAddress\": [\n"
                + "\n"
                + "                ],\n"
                + "                \"transactionHash\": \"0x3d30d01a470dc4ae92e87f4c0068d640f880ce27e8b863780cbcb748f1654939\",\n"
                + "                \"transactionPosition\": 1,\n"
                + "                \"type\": \"create\"\n"
                + "            }, {\n"
                + "                \"action\": {\n"
                + "                    \"callType\": \"call\",\n"
                + "                    \"from\": \"0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a\",\n"
                + "                    \"gas\": \"0x4f6c5\",\n"
                + "                    \"input\": \"0xb46300ec\",\n"
                + "                    \"to\": \"0x781ab1a38837e351bfe1e318c6587766848abffa\",\n"
                + "                    \"value\": \"0x0\"\n"
                + "                },\n"
                + "                \"error\": \"Bad instruction\",\n"
                + "                \"subtraces\": 1,\n"
                + "                \"traceAddress\": [\n"
                + "\n"
                + "                ],\n"
                + "                \"type\": \"call\"\n"
                + "            }, {\n"
                + "                \"action\": {\n"
                + "                    \"address\": \"0xb8d2ac822f3d0445f5b83d32b0b176c2cb3d0e60\",\n"
                + "                    \"balance\": \"0x0\",\n"
                + "                    \"refundAddress\": \"0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a\"\n"
                + "                },\n"
                + "                \"blockHash\": \"0xf263b9364434a781057c467004e8d398d915529bdffa6f600d6d17fe733d3210\",\n"
                + "                \"blockNumber\": 3740614,\n"
                + "                \"result\": null,\n"
                + "                \"subtraces\": 0,\n"
                + "                \"traceAddress\": [\n"
                + "                    0\n"
                + "                ],\n"
                + "                \"transactionHash\": \"0xea6649db0f88d5400159853bf2c5b752ce724435dfb85b35f8725cf4cdc1ad6d\",\n"
                + "                \"transactionPosition\": 2,\n"
                + "                \"type\": \"suicide\"\n"
                + "            }, {\n"
                + "                \"action\": {\n"
                + "                    \"author\": \"0xb8d2ac822f3d0445f5b83d32b0b176c2cb3d0e60\",\n"
                + "                    \"value\": \"0x0\",\n"
                + "                    \"rewardType\": \"reward\"\n"
                + "                },\n"
                + "                \"blockHash\": \"0x8d9aff92ab07598b65ca3457dc68ee66de48ea0e1d4b0a658b6e39977b2a8542\",\n"
                + "                \"blockNumber\": 5676554,\n"
                + "                \"subtraces\": 0,\n"
                + "                \"traceAddress\": [\n"
                + "                    0\n"
                + "                ],\n"
                + "                \"type\": \"reward\"\n"
                + "            }\n"
                + "        ]\n"
                + "    },\n"
                + "    \"id\": 1\n"
                + "}"
        );
        //CHECKSTYLE:ON

        ParityFullTraceResponse response = deserialiseResponse(ParityFullTraceResponse.class);
        assertNotNull(response);

        //CHECKSTYLE:OFF
        org.web3j.protocol.parity.methods.response.Trace trace1 = new org.web3j.protocol.parity.methods.response.Trace();
        Trace.CreateAction action1 = new Trace.CreateAction();
        action1.setFrom("0x6c24f4387b31251fd7b6d7a1269d880b2108bf3a");
        action1.setGas("0x4bc1f5");
        action1.setInit("0x606060405234156200000d57fe5b60405162004e4038038062004e40833981016040908152815");
        action1.setValue("0x0");
        trace1.setAction(action1);
        trace1.setBlockHash("0xf831abfdc67fb2ea3d16b6510b8e37d3d3e52ce3c25ed345053cb9503a430dd8");
        trace1.setBlockNumber(BigInteger.valueOf(4019912));
        trace1.setResult(new Trace.Result(
                "0x1c9997559f6f1ae2ece8525eab68709f50865165",
                "0x606060405236156102b95763ffffffff60e060020a600035041663095ea7b381146102bb578063",
                "0x3ef6fe",
                null
        ));
        trace1.setSubtraces(BigInteger.ZERO);
        trace1.setTraceAddress(Collections.emptyList());
        trace1.setTransactionHash("0x3d30d01a470dc4ae92e87f4c0068d640f880ce27e8b863780cbcb748f1654939");
        trace1.setTransactionPosition(BigInteger.ONE);
        trace1.setType("create");

        org.web3j.protocol.parity.methods.response.Trace trace2 = new org.web3j.protocol.parity.methods.response.Trace();
        Trace.CallAction action2 = new Trace.CallAction();
        action2.setCallType("call");
        action2.setFrom("0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a");
        action2.setTo("0x781ab1a38837e351bfe1e318c6587766848abffa");
        action2.setGas("0x4f6c5");
        action2.setInput("0xb46300ec");
        action2.setValue("0x0");
        trace2.setAction(action2);
        trace2.setError("Bad instruction");
        trace2.setSubtraces(BigInteger.ONE);
        trace2.setTraceAddress(Collections.emptyList());
        trace2.setType("call");

        org.web3j.protocol.parity.methods.response.Trace trace3 = new org.web3j.protocol.parity.methods.response.Trace();
        Trace.SuicideAction action3 = new Trace.SuicideAction();
        action3.setAddress("0xb8d2ac822f3d0445f5b83d32b0b176c2cb3d0e60");
        action3.setBalance("0x0");
        action3.setRefundAddress("0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a");
        trace3.setAction(action3);
        trace3.setBlockHash("0xf263b9364434a781057c467004e8d398d915529bdffa6f600d6d17fe733d3210");
        trace3.setBlockNumber(BigInteger.valueOf(3740614));
        trace3.setSubtraces(BigInteger.ZERO);
        trace3.setTraceAddress(Collections.singletonList(BigInteger.ZERO));
        trace3.setTransactionHash("0xea6649db0f88d5400159853bf2c5b752ce724435dfb85b35f8725cf4cdc1ad6d");
        trace3.setTransactionPosition(BigInteger.valueOf(2));
        trace3.setType("suicide");

        org.web3j.protocol.parity.methods.response.Trace trace4 = new org.web3j.protocol.parity.methods.response.Trace();
        Trace.RewardAction action4 = new Trace.RewardAction();
        action4.setAuthor("0xb8d2ac822f3d0445f5b83d32b0b176c2cb3d0e60");
        action4.setValue("0x0");
        action4.setRewardType("reward");
        trace4.setAction(action4);
        trace4.setBlockHash("0x8d9aff92ab07598b65ca3457dc68ee66de48ea0e1d4b0a658b6e39977b2a8542");
        trace4.setBlockNumber(BigInteger.valueOf(5676554));
        trace4.setSubtraces(BigInteger.ZERO);
        trace4.setTraceAddress(Collections.singletonList(BigInteger.ZERO));
        trace4.setType("reward");
        //CHECKSTYLE:ON

        List<org.web3j.protocol.parity.methods.response.Trace> traces = new ArrayList<>();
        traces.add(trace1);
        traces.add(trace2);
        traces.add(trace3);
        traces.add(trace4);
        FullTraceInfo info = new FullTraceInfo("0x", null, traces, null);
        assertThat(response.getFullTraceInfo(), equalTo(info));
    }

    @Test
    public void testParityFullTraceResponseVMTrace() {
        // hacked together from multiple requests and the code shortened
        //CHECKSTYLE:OFF
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"result\": {\n"
                + "        \"output\": \"0x\",\n"
                + "        \"vmTrace\": {\n"
                + "            \"code\": \"0x6060604052361561004a576000357c01\",\n"
                + "            \"ops\": [{\n"
                + "                    \"cost\": 20000,\n"
                + "                    \"ex\": {\n"
                + "                        \"mem\": null,\n"
                + "                        \"push\": [\n"
                + "\n"
                + "                        ],\n"
                + "                        \"store\": {\n"
                + "                            \"key\": \"0x0\",\n"
                + "                            \"val\": \"0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a\"\n"
                + "                        },\n"
                + "                        \"used\": 241835\n"
                + "                    },\n"
                + "                    \"pc\": 79,\n"
                + "                    \"sub\": null\n"
                + "                }, {\n"
                + "                    \"cost\": 9700,\n"
                + "                    \"ex\": {\n"
                + "                        \"mem\": {\n"
                + "                            \"data\": \"0x\",\n"
                + "                            \"off\": 96\n"
                + "                        },\n"
                + "                        \"push\": [\n"
                + "                            \"0x1\"\n"
                + "                        ],\n"
                + "                        \"store\": null,\n"
                + "                        \"used\": 317494\n"
                + "                    },\n"
                + "                    \"pc\": 337,\n"
                + "                    \"sub\": {\n"
                + "                        \"code\": \"0x606060405236156100b75763ffffffff\",\n"
                + "                        \"ops\": [{\n"
                + "                                \"cost\": 3,\n"
                + "                                \"ex\": {\n"
                + "                                    \"mem\": null,\n"
                + "                                    \"push\": [\n"
                + "                                        \"0x60\"\n"
                + "                                    ],\n"
                + "                                    \"store\": null,\n"
                + "                                    \"used\": 5753235\n"
                + "                                },\n"
                + "                                \"pc\": 0,\n"
                + "                                \"sub\": null\n"
                + "                            }\n"
                + "                        ]\n"
                + "                    }\n"
                + "                }\n"
                + "            ]\n"
                + "        }\n"
                + "    },\n"
                + "    \"id\": 1\n"
                + "}"
        );
        //CHECKSTYLE:ON

        ParityFullTraceResponse response = deserialiseResponse(ParityFullTraceResponse.class);
        assertNotNull(response);

        VMTrace.VMOperation operation1 = new VMTrace.VMOperation(
                null,
                BigInteger.valueOf(20000),
                new VMTrace.VMOperation.Ex(
                        null,
                        Collections.emptyList(),
                        new VMTrace.VMOperation.Ex.Store(
                                "0x0",
                                "0x14772e4f805b4dd2e69bd6d3f9b5edf0dfa5385a"),
                        BigInteger.valueOf(241835)),
                BigInteger.valueOf(79));

        VMTrace.VMOperation subOperation = new VMTrace.VMOperation(
                null,
                BigInteger.valueOf(3),
                new VMTrace.VMOperation.Ex(
                        null,
                        Collections.singletonList("0x60"),
                        null,
                        BigInteger.valueOf(5753235)),
                BigInteger.ZERO);

        VMTrace.VMOperation operation2 = new VMTrace.VMOperation(
                new VMTrace(
                        "0x606060405236156100b75763ffffffff",
                        Collections.singletonList(subOperation)),
                BigInteger.valueOf(9700),
                new VMTrace.VMOperation.Ex(
                        new VMTrace.VMOperation.Ex.Mem(
                                "0x",
                                BigInteger.valueOf(96)),
                        Collections.singletonList("0x1"),
                        null,
                        BigInteger.valueOf(317494)),
                BigInteger.valueOf(337));

        VMTrace trace = new VMTrace(
                "0x6060604052361561004a576000357c01",
                Arrays.asList(operation1, operation2));
        FullTraceInfo info = new FullTraceInfo("0x", null, null, trace);
        assertThat(response.getFullTraceInfo(), equalTo(info));
    }

}
