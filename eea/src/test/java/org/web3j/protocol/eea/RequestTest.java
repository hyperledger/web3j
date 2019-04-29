package org.web3j.protocol.eea;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class RequestTest extends RequestTester {
    private Eea web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Eea.build(httpService);
    }

    @Test
    public void testEthSendRawTransaction() throws Exception {
        web3j.eeaSendRawTransaction(
                "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f"
                        + "072445675058bb8eb970870f072445675").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_sendRawTransaction\",\"params\":[\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testEeaGetTransactionReceipt() throws Exception {
        web3j.eeaGetTransactionReceipt("0x123").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_getTransactionReceipt\","
                + "\"params\":[\"0x123\"],\"id\":1}");
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        web3j.eeaGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                "0x0").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_getTransactionCount\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"0x0\"],"
                + "\"id\":1}");
    }

    @Test
    public void test() {
        final String privacyGroupId = generatePrivacyGroupId("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=", Collections.singletonList("Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="));
        assertThat(privacyGroupId, is("0x4479414f69462f796e70632b4a586132594147423062436974536c4f4d4e6d2b53686d422f374d364334773d"));
    }

    private String generatePrivacyGroupId(final String privateFrom, final List<String> privateFor) {
        final List<byte[]> stringList = new ArrayList<>();
        stringList.add(Base64.getDecoder().decode(privateFrom));
        privateFor.forEach(item -> stringList.add(Base64.getDecoder().decode(item)));

        final List<RlpType> rlpList = stringList.stream().distinct()
                .sorted(Comparator.comparing(Arrays::hashCode))
                .map(RlpString::create).collect(Collectors.toList());

        return Numeric.toHexString(
                Base64.getEncoder().encode(
                        Hash.sha3(
                                RlpEncoder.encode(new RlpList(rlpList)))));
    }

}
