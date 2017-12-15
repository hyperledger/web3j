package org.web3j.benchmark;

import org.apache.commons.cli.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

public class Benchmark {
    private Config config;
    private List<Web3j> services;
    private ExecutorService executor;

    public Benchmark(String configPath) throws java.io.IOException {
        this.config = Config.load(configPath);
        this.services = new ArrayList<>();
        this.config.getUrls().forEach((url) -> {
            this.services.add(Web3j.build(new HttpService(url), 3000));
        });
        this.executor = Executors.newWorkStealingPool(this.config.getThreadNumber());
    }

    private void run() throws Exception {
        switch (this.config.getType()) {
            case BlockNumber:
                BlockNumberTest blockNumberTest = new BlockNumberTest(this.services, this.config.getTxNumber(), this.executor);
                blockNumberTest.start();
                blockNumberTest.statistic();
                break;
            case SendTransaction:
                SendTransactionTest sendTransactionTest = new SendTransactionTest(this.services, this.config, this.executor);
                sendTransactionTest.start();
                sendTransactionTest.statistic();
                break;
            case Analysis:
                Analysis analyse = new Analysis(this.services);
                analyse.statistic();
                break;
            case SendTransactionAndGetReceipt:
                TransactionReceiptTest transactionReceiptTest = new TransactionReceiptTest(this.services, this.config, this.executor);
                transactionReceiptTest.start();
                transactionReceiptTest.statistic();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("config", true, "config file which contains bench test info");
        options.addOption("interface", "test all cita-supported jsonrpc interface");
        options.addOption("token", true, "test transaction execute by send token randomly between users");
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption("interface")) {
            new InterfaceTest(Web3j.build(new HttpService("http://localhost:1337"))).run();
            return;
        }

        if (commandLine.hasOption("config")) {
            String file = commandLine.getOptionValue("config");
            Benchmark bench = new Benchmark(file );
            bench.run();
            return;
        }

        if (commandLine.hasOption("token")) {
            String file = commandLine.getOptionValue("token");
            TokenTest test = new TokenTest(Web3j.build(new HttpService("http://localhost:1337")), file);
            test.run();
            return;
        }
    }

    public static long getBlockNumber(Web3j service) {
        try {
            return service.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (Throwable e) {
            System.out.println("get block number failed, " + e);
            System.exit(1);
            return 0;
        }
    }

    // Note: t1 < t2
    public static double diff(LocalDateTime t1, LocalDateTime t2) {
        ZoneId zoneId = ZoneId.systemDefault();
        long diffSecs = t2.atZone(zoneId).toEpochSecond() - t1.atZone(zoneId).toEpochSecond();
        long diffNanos = diffSecs * 1000_000_000;
        return (diffNanos + t2.getNano() - t1.getNano()) / 1000_000.0;
    }

    public static long dateTimeAsMills(LocalDateTime t) {
        ZoneId zoneId = ZoneId.systemDefault();
        long secs = t.atZone(zoneId).toEpochSecond();
        return secs * 1000 + t.getNano() / 1000_000;
    }

    public static <T> CompletableFuture<T> runAsync(Callable<T> callable, ExecutorService executor) {
        CompletableFuture<T> result = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                result.complete(callable.call());
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        }, executor);
        return result;
    }

    public static <T> void writeToFile(T result, String path) throws java.io.IOException {
        ObjectMapper obj = new ObjectMapper();
        obj.writerWithDefaultPrettyPrinter().writeValue(new File(path), result);
    }

    public static <T> String serializeToJson(T value) throws Exception {
        ObjectMapper obj = new ObjectMapper();
        return obj.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }

    public static class JsonRpcBenchResult {
        public Config.BenchType type;
        public long txNumber;
        public long succeed;
        public long failed;
        public long startHeight;
        public long endHeight;
        public String startTime;
        public String endTime;
        public double spend;
        public double tps;

        public static JsonRpcBenchResult load(String path) throws Exception {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(new File(path), JsonRpcBenchResult.class);
        }
    }
}
