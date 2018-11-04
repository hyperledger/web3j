package org.web3j.protocol.pantheon;

import org.junit.Test;
import org.web3j.protocol.RequestTester;
import org.web3j.protocol.http.HttpService;

import java.util.HashMap;
import java.util.Map;

public class RequestTest extends RequestTester {
    private Pantheon web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Pantheon.build(httpService);
    }

    @Test
    public void testMinerStart() throws Exception {
        web3j.minerStart().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"miner_start\","
                + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testMinerStop() throws Exception {
        web3j.minerStop().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"miner_stop\","
                + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testTraceTransaction() throws Exception {

        String transactionHash = "0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e";

        Map <String,Boolean> options=  new HashMap <>();
        options.put("disableStorage", false);
        options.put("disableStack",false);
        options.put("disableMemory",true);

        web3j.debugTraceTransaction(transactionHash, options).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"debug_traceTransaction\","
                + "\"params\":[\"0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e\","
                + "{\"disableMemory\":true,"
                + "\"disableStorage\":false,"
                + "\"disableStack\":false}],"
                + "\"id\":1}");
        //CHECKSTYLE:ON

    }
}
