package org.web3j.benchmark;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionReceiptTest {
    private Config config;
    private List<Web3j> services;
    private long startTime;
    private long endTime;
    private ExecutorService executor;
    private SendTransactionTest sendTransactionTest;
    private Analysis analysis;
    private long startHeight;
    private long endHeight;
    private AtomicLong succeed;
    private AtomicLong failed;

    public TransactionReceiptTest(List<Web3j> services, Config config, ExecutorService executor) throws java.io.IOException {
        this.sendTransactionTest = new SendTransactionTest(services, config, executor);
        this.analysis = new Analysis(services);
        this.config = config;
        this.services = services;
        this.executor = executor;
        this.startHeight = 0;
        this.endHeight = 0;
        this.succeed = new AtomicLong(0);
        this.failed = new AtomicLong(0);
    }

    public void start() throws Exception {
        this.sendTransactionTest.start();
        this.sendTransactionTest.statistic();
        this.analysis.statistic();
    }

    public void statistic() throws Exception {
        Benchmark.JsonRpcBenchResult jsonRpcBenchResult = Benchmark.JsonRpcBenchResult.load("./JsonRpcBenchResult.json");
        LocalDateTime startTime = LocalDateTime.parse(jsonRpcBenchResult.startTime);
        this.startTime = Benchmark.dateTimeAsMills(startTime);
        this.startHeight = jsonRpcBenchResult.startHeight;

        Analysis.TxIntoChainBenchResult benchResult = Analysis.TxIntoChainBenchResult.load("./TxIntoChainBenchResult.json");
        this.endTime = benchResult.endTime;
        this.endHeight = benchResult.endHeight;

        long blockNumber = benchResult.startHeight;
        int serviceSize = this.services.size();
        while (blockNumber <= this.endHeight) {
            int pos = (int)blockNumber % serviceSize;
            Web3j service = this.services.get(pos);
            EthBlock.Block block = service
                    .ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), false)
                    .send()
                    .getBlock();
            if (block == null) {
                continue;
            }

            block.getBody()
                 .getTransactions()
                 .forEach((txResult) -> {
                     EthBlock.TransactionHash txHash = (EthBlock.TransactionHash)txResult;
                     Benchmark.runAsync(() -> service.ethGetTransactionReceipt(txHash.get()).send(), this.executor).thenAccept((receipt) -> {
                         if (receipt.hasError()) {
                             this.failed.incrementAndGet();
                         } else {
                             if (receipt.getTransactionReceipt().isPresent() && (receipt.getTransactionReceipt().get().getErrorMessage() == null)) {
                                 this.succeed.incrementAndGet();
                             } else {
                                 this.failed.incrementAndGet();
                             }
                         }
                     });
                 });
            blockNumber += 1;
        }

        System.out.println("wait to get receipt");
        this.executor.shutdown();
        this.executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        TransactionReceiptBenchResult result = generateResult();
        Benchmark.writeToFile(result, "./TransactionReceiptBenchResult.json");
        System.out.println("bench result: " + Benchmark.serializeToJson(result));
    }

    private TransactionReceiptBenchResult generateResult() {
        TransactionReceiptBenchResult benchResult = new TransactionReceiptBenchResult();
        benchResult.type = Config.BenchType.SendTransactionAndGetReceipt;
        benchResult.txNumber = this.config.getTxNumber();
        benchResult.executedFailed = this.failed.get();
        benchResult.exectedSucceed = this.succeed.get();
        benchResult.startHeight = this.startHeight;
        benchResult.endHeight = this.endHeight;
        benchResult.startTime = this.startTime;
        benchResult.endTime = this.endTime;
        benchResult.spend = (double)this.endTime - this.startTime;
        benchResult.tps = (this.config.getTxNumber() * 1000) / benchResult.spend;
        return benchResult;
    }

    private static class TransactionReceiptBenchResult {
        public Config.BenchType type;
        public long txNumber;
        public long exectedSucceed;
        public long executedFailed;
        public long startHeight;
        public long endHeight;
        public long startTime;
        public long endTime;
        public double spend;
        public double tps;
    }
}
