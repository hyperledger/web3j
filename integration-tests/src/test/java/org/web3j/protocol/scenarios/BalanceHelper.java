package org.web3j.protocol.scenarios;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.TestParameters;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import static org.web3j.protocol.scenarios.Scenario.ALICE;
import static org.web3j.protocol.scenarios.Scenario.BOB;

/**
 * This rebalances the balance of ALICE and BOB equally amongst themselves.
 *
 * <p>The tests should be created so that any transfer from ALICE to BOB should be offset by a
 * transfer from BOB to ALICE to minimize the likelihood that one of them would ran out of
 * ether.
 *
 * <p>But if in case the balance of ALICE or BOB has become insufficient (or if in case we just
 * want to rebalance their balances equally amongst themselves), then we can run the
 * {@link #main(String[])} of this class manually to do that.
 */
public class BalanceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceHelper.class);

    public static void main(String[] args)
            throws Exception {

        String url = args.length > 0 ? TestParameters.TEST_RINKEBY_URL : args[0];
        LOGGER.info("Connecting to {}", url);

        HttpService httpService = new HttpService(url);
        if (args.length >= 3) {
            LOGGER.info("Adding authorization URL to {}", url);
            httpService.addHeader("Authorization", okhttp3.Credentials.basic(args[1], args[2]));
        }
        Admin web3j = Admin.build(httpService);

        LOGGER.info("Retrieving latest balances of ALICE and BOB");

        LOGGER.info("Retrieving latest balance of ALICE ({})", ALICE.getAddress());
        BigInteger aliceBalance = web3j.ethGetBalance(
                ALICE.getAddress(), DefaultBlockParameter.valueOf("latest")).send()
                .getBalance();
        LOGGER.info("ACLICE's latest balance = {}", aliceBalance);

        LOGGER.info("Retrieving latest balance of BOB ({})", BOB.getAddress());
        BigInteger bobBalance = web3j.ethGetBalance(
                BOB.getAddress(), DefaultBlockParameter.valueOf("latest")).send()
                .getBalance();
        LOGGER.info("BOB's latest balance = {}", bobBalance);

        BigInteger total = aliceBalance.add(bobBalance);
        BigInteger equalBalance = total.divide(BigInteger.valueOf(2));

        LOGGER.info("Total balance = {}", total);
        LOGGER.info("Equal balance = {}", equalBalance);

        Credentials sender = aliceBalance.compareTo(equalBalance) > 0 ? ALICE : BOB;
        Credentials recipient = sender == ALICE ? BOB : ALICE;

        BigInteger sendersBalance = sender == ALICE ? aliceBalance : bobBalance;
        BigDecimal transferAmount = new BigDecimal(sendersBalance.subtract(equalBalance));

        LOGGER.info("Transferring {} WEI from {} ({}) to {} ({})",
                transferAmount,
                sender == ALICE ? "ALICE" : "BOB",
                sender.getAddress(),
                recipient == ALICE ? "ALICE" : "BOB",
                recipient.getAddress()
        );

        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j,
                sender,
                recipient.getAddress(),
                transferAmount,
                Convert.Unit.WEI)
                .send();

        LOGGER.info("TransactionReceipt = {}", transactionReceipt);
    }
}
