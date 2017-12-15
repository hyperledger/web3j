package org.web3j.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.web3j.benchmark.Benchmark.serializeToJson;
import static org.web3j.benchmark.Benchmark.writeToFile;

public class Analysis {
    private List<Web3j> services;

    public Analysis(List<Web3j> services) {
        this.services = services;
    }

    public void statistic() throws Exception {
        Benchmark.JsonRpcBenchResult benchResult = Benchmark.JsonRpcBenchResult.load("./JsonRpcBenchResult.json");
        long blockNumber = benchResult.startHeight;
        long txNumber = 0;
        long startTime = 0;
        long startHeight = 0;
        long lastBlockTimestamp = 0;
        long totalQuota = 0;
        EthBlock.Block block = null;
        List<Analysis.BlockInfo> blocks = new LinkedList<>();
        while (txNumber < benchResult.succeed) {
            block = getBlockByNumber(blockNumber);
            if (block == null) {
                Thread.sleep(3000);
                continue;
            }

            Analysis.BlockInfo blockInfo = new Analysis.BlockInfo();
            long blockTimestamp = block.getHeader().getTimestamp();
            if (txNumber == 0) {
                startTime = blockTimestamp;
                startHeight = blockNumber;
            }
            blockInfo.quotaUsed = Numeric.decodeQuantity(block.getHeader().getGasUsed()).longValue();
            blockInfo.height = blockNumber;
            long blockTxNumber = block.getBody().getTransactions().size();
            blockInfo.txNumber = blockTxNumber;
            blockInfo.spend = blockTimestamp - lastBlockTimestamp;
            txNumber += blockTxNumber;
            blockNumber += 1;
            lastBlockTimestamp = blockTimestamp;
            blocks.add(blockInfo);
            totalQuota += blockInfo.quotaUsed;
        }

        Analysis.TxIntoChainBenchResult result = new Analysis.TxIntoChainBenchResult();
        result.startHeight = startHeight;
        result.endHeight = blockNumber - 1;
        result.startTime = startTime;
        result.endTime = block.getHeader().getTimestamp();
        result.spend = result.endTime - result.startTime;
        double millis = (double) result.spend;
        result.tps = (txNumber * 1000) / millis;
        result.qps = (totalQuota * 1000) / millis;
        result.blocks = blocks.stream().filter(elem -> elem.height >= result.startHeight).collect(Collectors.toList());
        writeToFile(result, "./TxIntoChainBenchResult.json");
        System.out.println("bench result: " + serializeToJson(result));
    }

    private EthBlock.Block getBlockByNumber(long blockNumber) {
        try {
            return this.services.get(0)
                    .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), false)
                    .send()
                    .getBlock();
        } catch (Throwable e) {
            System.out.println("get block " + blockNumber + " failed because of " + e);
            return null;
        }
    }

    public static class TxIntoChainBenchResult {
        public long startHeight;
        public long endHeight;
        public long startTime;
        public long endTime;
        public long spend;
        public double tps;
        public double qps;
        public List<Analysis.BlockInfo> blocks;

        public static TxIntoChainBenchResult load(String path) throws Exception {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(new File(path), TxIntoChainBenchResult.class);
        }
    }

    private static class BlockInfo {
        public long quotaUsed;
        public long height;
        public long spend;
        public long txNumber;
    }
}
