package org.web3j.benchmark;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static org.web3j.benchmark.Benchmark.diff;
import static org.web3j.benchmark.Benchmark.serializeToJson;

public class SendTransactionTest {
    private Config config;
    private long currentHeight;
    private List<Web3j> services;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExecutorService executor;
    private BlockingQueue<EthSendTransaction> queue;
    private Random random;
    private long startHeight;
    private long endHeight;
    private long succeed;
    private long failed;

    public SendTransactionTest(List<Web3j> services, Config config, ExecutorService executor) throws java.io.IOException {
        this.config = config;
        this.services = services;
        this.queue = new ArrayBlockingQueue<>(config.getTxNumber());
        this.executor = executor;
        this.random = new Random(System.currentTimeMillis());
        this.startHeight = 0;
        this.endHeight = 0;
        this.succeed = 0;
        this.failed = 0;
        this.currentHeight = 0;
    }

    private String newTransaction() {
        Transaction tx;
        long nonce = Math.abs(this.random.nextLong());
        if (this.config.getTo().equals("")) {
            tx = Transaction.createContractTransaction(BigInteger.valueOf(nonce), this.config.getQuota(), this.currentHeight + 88, this.config.getCode());
        } else {
            tx = Transaction.createFunctionCallTransaction(this.config.getTo(), BigInteger.valueOf(nonce), this.config.getQuota(), this.currentHeight + 88, this.config.getCode());
        }
        return tx.sign(this.config.getPrivateKey());
    }

    private List<String> generateTransactions() {
        int duration = 10000;
        int txNumber = this.config.getTxNumber();
        List<String> txs = new ArrayList<>(txNumber);
        for (int i = 0; i < txNumber; i++) {
            if (i % duration == 0) {
                this.currentHeight = Benchmark.getBlockNumber(this.services.get(0));
            }
            txs.add(newTransaction());
        }
        return txs;
    }

    private boolean isSuccess(EthSendTransaction result) {
        if (result.hasError()) {
            System.out.println("send transaction failed because of " + result.getError().getMessage());
            return false;
        } else {
            String hash = result.getSendTransactionResult().getHash();
            String status = result.getSendTransactionResult().getStatus();
            if (status.equals("Ok")) {
                return true;
            } else {
                System.out.println("tx " + hash + "failed, because of " + status);
                return false;
            }
        }
    }

    public void start() {
        System.out.println("generate transactions");
        List<String> txs = generateTransactions();
        this.startHeight = Benchmark.getBlockNumber(this.services.get(0));
        System.out.println("start send transaction");
        this.startTime = LocalDateTime.now();
        for (int i = 0; i < this.config.getTxNumber(); i++) {
            int pos = i % this.services.size();
            String signedTx = txs.get(i);
            Benchmark.runAsync(() -> this.services.get(pos).ethSendRawTransaction(signedTx).send(), this.executor).thenAccept((result) -> {
                try {
                    this.queue.put(result);
                } catch (InterruptedException e) {
                    System.out.println("enqueue tx response failed, continue");
                }
            });
        }
    }

    public void statistic() throws Exception {
        while (true) {
            EthSendTransaction response = this.queue.take();
            if (isSuccess(response)) {
                this.succeed += 1;
            } else {
                this.failed += 1;
            }

            if ((this.failed + this.succeed) >= this.config.getTxNumber()) {
                this.endTime = LocalDateTime.now();
                this.endHeight = Benchmark.getBlockNumber(this.services.get(0));
                Benchmark.JsonRpcBenchResult result = generateResult();
                Benchmark.writeToFile(result, "./JsonRpcBenchResult.json");
                System.out.println("bench result: " + serializeToJson(result));
                break;
            }
        }
    }

    private Benchmark.JsonRpcBenchResult generateResult() {
        Benchmark.JsonRpcBenchResult result = new Benchmark.JsonRpcBenchResult();
        double diffMillis = diff(this.startTime, this.endTime);
        result.type = Config.BenchType.SendTransaction;
        result.startTime = this.startTime.toString();
        result.endTime = this.endTime.toString();
        result.spend = diffMillis;
        result.startHeight = this.startHeight;
        result.endHeight = this.endHeight;
        result.tps = (this.config.getTxNumber() * 1000) / diffMillis;
        result.succeed = this.succeed;
        result.failed = this.failed;
        result.txNumber = this.config.getTxNumber();
        return result;
    }
}
