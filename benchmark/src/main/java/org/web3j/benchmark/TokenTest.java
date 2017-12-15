package org.web3j.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.CitaTransactionManager;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TokenTest {
    private final long initialSupply = 1000000;
    private final Random random = new Random(System.currentTimeMillis());
    private Map<Credentials, Long> accounts;
    private Credentials creator;
    private Web3j service;
    private Token token;

    public TokenTest(Web3j service, String path) throws Exception {
        Accounts accounts = Accounts.load(path);
        this.service = service;
        this.creator = Credentials.create(accounts.creator);
        this.accounts = new HashMap<>();
        long init = 0;
        accounts.others.stream().map(Credentials::create).forEach(c -> this.accounts.put(c, init));
        accounts.others.forEach((account) -> System.out.println(account + ": " + Credentials.create(account).getAddress()));
    }

    public void run() throws Exception {
        CitaTransactionManager creatorManager = transactionManagerOf(this.creator);
        System.out.println("Now, start token test");
        CompletableFuture<Token> tokenFuture = Token.deploy(service, creatorManager, BigInteger.valueOf(1000000), nextNonce(), BigInteger.valueOf(initialSupply)).sendAsync();
        tokenFuture.whenCompleteAsync((contract, exception) -> {
            if (exception != null) {
                System.out.println("deploy contract failed because of " + exception);
                exception.printStackTrace();
                System.exit(1);
            }

            this.token = contract;
            System.out.println("deploy success, contract address: " + this.token.getContractAddress());
            try {
                testAccountsInit();
                this.accounts.put(this.creator, this.initialSupply);
                randomTransferToken();
            } catch (Throwable e) {
                System.out.println("test failed because of " + e);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    // when start, account hava 0 tokens, creator have initialSupply tokens
    private void testAccountsInit() throws Exception {
        // First, test if the creator have initialSupply tokens
        CompletableFuture<BigInteger> tokensOfCreator = this.token.balanceOf(creator.getAddress()).sendAsync();
        BigInteger tokens = tokensOfCreator.get(8, TimeUnit.SECONDS);
        long balanceOfCreator = tokens.longValue();
        assert(balanceOfCreator == initialSupply);

        this.accounts.keySet().forEach((credentials -> {
            assert(balanceOf(credentials) == 0);
        }));
    }

    private void randomTransferToken() {
        ArrayList<Credentials> credentials = new ArrayList<>(this.accounts.keySet());
        int accountNum = credentials.size();
        Random random = new Random();
        long shuffleThreshold = 10;
        new Thread(this::eventObserve).start();
        new Thread(this::blockObserve).start();

        while (true) {
            int[] pair = random.ints(0, accountNum).limit(2).toArray();
            Credentials from;
            Credentials to;
            if (this.accounts.get(credentials.get(pair[0])) > this.accounts.get(credentials.get(pair[1]))) {
                from = credentials.get(pair[0]);
                to = credentials.get(pair[1]);
            } else {
                from = credentials.get(pair[1]);
                to = credentials.get(pair[0]);
            }
            long balanceOfFrom = this.accounts.get(from);
            long balanceOfTo = this.accounts.get(to);
            if (balanceOfFrom == balanceOfTo) {
                continue;
            }

            long transfer = ThreadLocalRandom.current().nextLong(0, balanceOfFrom - balanceOfTo);
            TransferEvent event = new TransferEvent(from, to, transfer);
            CompletableFuture<TransactionReceipt> receiptFuture = event.execute(this.service);
            try {
                TransactionReceipt receipt = receiptFuture.get(12, TimeUnit.SECONDS);
                if (receipt.getErrorMessage() == null) {
                    System.out.println(event + " execute success");
                    updateAccount(event);
                } else {
                    System.out.println(event + " execute failed, " + receipt.getErrorMessage());
                }
            } catch (InterruptedException|ExecutionException|TimeoutException e) {
                System.out.println("Transaction " + event + ", get receipt failed, " + e);
                waitToGetToken();
            }

            if (!isTokenConserve()) {
                System.out.println("token test failed, account tokens: ");
                credentials.forEach((account) -> {
                    long accountBalance = balanceOf(account);
                    System.out.println(account.getAddress() + ": " + accountBalance);
                });
                System.exit(1);
            }

            if (event.tokens < shuffleThreshold) {
                shuffle(credentials.get(0));
            }
        }
    }

    // When get receipt failed because of timeout, we need to wait contract executed completely.
    private void waitToGetToken() {
        try {
            while (!isTokenConserve()) {
                System.out.println("wait to get account token");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
        }

        ArrayList<Credentials> credentials = new ArrayList<>(this.accounts.keySet());
        credentials.forEach((account) -> {
            long accountBalance = balanceOf(account);
            this.accounts.put(account, accountBalance);
        });
    }

    private boolean isTokenConserve() {
        Map<Credentials, Long> accountTokens = this.accounts.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::balanceOf
                ));
        long zero = 0;
        long totalToken = accountTokens.values().stream().reduce(zero, (x, y) -> x + y);
        return totalToken == this.initialSupply;
    }

    private void shuffle(Credentials credentials) {
        System.out.println("transfer all tokens to " + credentials.getAddress());
        this.accounts.forEach((account, balance) -> {
            if (account != credentials) {
                TransferEvent event = new TransferEvent(account, credentials, balance);
                CompletableFuture<TransactionReceipt> receiptFuture = event.execute(this.service);
                try {
                    TransactionReceipt receipt = receiptFuture.get(12, TimeUnit.SECONDS);
                    if (receipt.getErrorMessage() == null) {
                        System.out.println(event + " execute success");
                        updateAccount(event);
                    } else {
                        System.out.println(event + " execute failed, " + receipt.getErrorMessage());
                    }
                } catch (InterruptedException|ExecutionException|TimeoutException e) {
                    System.out.println("get receipt failed, " + e);
                }
            }
        });
        this.accounts.keySet().forEach((account) -> {
            long accountBalance = balanceOf(account);
            this.accounts.put(account, accountBalance);
        });
    }

    private void eventObserve() {
        rx.Observable<Token.TransferEventResponse> observable = this.token.transferEventObservable(DefaultBlockParameter.valueOf(BigInteger.ONE), DefaultBlockParameter.valueOf("latest"));
        observable.subscribe(event -> System.out.println("Observable, TransferEvent(" + event.from + ", " + event.to + ", " + event.tokens.longValue() + ")"));
    }

    private void blockObserve() {
        rx.Observable<EthBlock> observable = this.service.blockObservable(false);
        observable.subscribe(block -> System.out.println("Observable, Block " + block.getBlock().getHeader().getNumber()));
    }

    private void updateAccount(TransferEvent event) {
        this.accounts.compute(event.from, (k, v) -> {
            assert(v >= event.tokens);
            return v - event.tokens;
        });
        this.accounts.compute(event.to, (k, v) -> v + event.tokens);
    }

    private long balanceOf(Credentials credentials) {
        long balance = Long.MAX_VALUE;
        Token token = tokenOf(credentials);
        CompletableFuture<BigInteger> future = token.balanceOf(credentials.getAddress()).sendAsync();
        try {
            balance = future.get().longValue();
        } catch (InterruptedException|ExecutionException e) {
            System.out.println("get balance of " + credentials.getAddress() + " failed because of " + e);
        }
        return balance;
    }

    private CitaTransactionManager transactionManagerOf(Credentials credentials) {
        CitaTransactionManager manager = new CitaTransactionManager(service, credentials, 5, 3000);
        try {
            long currentHeight = this.service.ethBlockNumber().send().getBlockNumber().longValue();
            manager.setCurrentHeight(currentHeight);
        } catch (IOException e) {
            System.out.println("get block number failed because of " + e);
            System.exit(1);
        }
        return manager;
    }

    private Token tokenOf(Credentials credentials) {
        CitaTransactionManager manager = transactionManagerOf(credentials);
        return Token.load(this.token.getContractAddress(), this.service, manager, BigInteger.valueOf(100000), nextNonce());
    }

    public BigInteger nextNonce() {
        return BigInteger.valueOf(Math.abs(this.random.nextLong()));
    }

    private class TransferEvent {
        Credentials from;
        Credentials to;
        long tokens;

        TransferEvent(Credentials from, Credentials to, long tokens) {
            this.from = from;
            this.to = to;
            this.tokens = tokens;
        }

        CompletableFuture<TransactionReceipt> execute(Web3j service) {
            Token token = TokenTest.this.tokenOf(this.from);
            return token.transfer(this.to.getAddress(), BigInteger.valueOf(tokens)).sendAsync();
        }

        @Override
        public String toString() {
            return "TransferEvent(" + this.from.getAddress() + ", " + this.to.getAddress() + ", " + this.tokens + ")";
        }
    }

    private static class Accounts {
        public String creator;
        public List<String> others;

        static Accounts load(String path) throws java.io.IOException {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(new File(path), Accounts.class);
        }
    }
}
