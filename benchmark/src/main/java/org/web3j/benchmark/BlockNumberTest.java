package org.web3j.benchmark;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static org.web3j.benchmark.Benchmark.diff;
import static org.web3j.benchmark.Benchmark.serializeToJson;

public class BlockNumberTest {
    private List<Web3j> services;
    private int questNumber;
    private ExecutorService executor;
    private long startHeight;
    private long endHeight;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BlockingQueue<Boolean> queue;
    private int succeed;
    private int failed;

    public BlockNumberTest(List<Web3j> services, int number, ExecutorService executor) {
        this.services = services;
        this.questNumber = number;
        this.executor = executor;
        this.startHeight = 0;
        this.queue = new ArrayBlockingQueue<Boolean>(number);
    }

    public void start() throws Exception {
        System.out.println("get block number");
        this.startHeight = Benchmark.getBlockNumber(services.get(0));
        this.startTime = LocalDateTime.now();
        for (int i = 0; i < this.questNumber; i++) {
            int pos = i % this.services.size();
            Benchmark.runAsync(() -> this.services.get(pos).ethBlockNumber().send(), this.executor).thenAccept((result) -> {
                this.complete(result);
            });
        }
    }

    private void complete(EthBlockNumber result) {
        try {
            this.queue.put(!result.hasError());
        } catch (Throwable e) {
            System.out.println("put result into queue failed");
            System.exit(1);
        }
    }

    public void statistic() throws Exception {
        while (true) {
            Boolean isSuccess = this.queue.take();
            if (isSuccess) {
                this.succeed += 1;
            } else {
                this.failed += 1;
            }

            if ((this.failed + this.succeed) >= this.questNumber) {
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
        result.type = Config.BenchType.BlockNumber;
        result.startTime = this.startTime.toString();
        result.endTime = this.endTime.toString();
        result.spend = diffMillis;
        result.startHeight = this.startHeight;
        result.endHeight = this.endHeight;
        result.tps = (this.questNumber * 1000) / diffMillis;
        result.succeed = this.succeed;
        result.failed = this.failed;
        result.txNumber = questNumber;
        return result;
    }
}
