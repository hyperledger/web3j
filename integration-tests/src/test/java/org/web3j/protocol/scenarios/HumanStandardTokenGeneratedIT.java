package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.web3j.crypto.Credentials;
import rx.Subscription;

import org.web3j.generated.HumanStandardToken;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;
import static org.web3j.generated.HumanStandardToken.ApprovalEventResponse;
import static org.web3j.generated.HumanStandardToken.TransferEventResponse;
import static org.web3j.generated.HumanStandardToken.deploy;
import static org.web3j.protocol.core.TestParameters.isInfuraTestRinkebyUrl;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

/**
 * Generated HumanStandardToken integration test for all supported scenarios.
 */
@RunWith(Parameterized.class)
public class HumanStandardTokenGeneratedIT extends Scenario {

    private final Credentials sender;
    private final Credentials recipient;

    public HumanStandardTokenGeneratedIT(
            @SuppressWarnings("unused") String ignoredTestName,
            Credentials sender,
            Credentials recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    @Test
    public void testContract() throws Exception {
        assumeFalse("Infura does NOT support eth_newFilter, "
                + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());
        BigInteger senderQty = BigInteger.valueOf(1_000_000);
        final String senderAddress = sender.getAddress();
        final String recipientAddress = recipient.getAddress();

        HumanStandardToken contract = deploy(web3j, sender,
                GAS_PRICE, GAS_LIMIT,
                senderQty, "web3j tokens",
                BigInteger.valueOf(18), "w3j$").send();

        assertTrue(contract.isValid());

        assertThat(contract.totalSupply().send(), equalTo(senderQty));

        assertThat(contract.balanceOf(sender.getAddress()).send(),
                equalTo(senderQty));

        // CHECKSTYLE:OFF
        CountDownLatch transferEventCountDownLatch = new CountDownLatch(2);
        Subscription transferEventSubscription = contract.transferEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                transferEventResponse -> transferEventCountDownLatch.countDown()
        );

        CountDownLatch approvalEventCountDownLatch = new CountDownLatch(1);
        Subscription approvalEventSubscription = contract.approvalEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                transferEventResponse -> transferEventCountDownLatch.countDown()
        );
        // CHECKSTYLE:ON

        // transfer tokens
        BigInteger transferQuantity = BigInteger.valueOf(100_000);

        TransactionReceipt senderTransferReceipt = contract.transfer(
                recipient.getAddress(), transferQuantity).send();

        TransferEventResponse senderTransferEventValues =
                contract.getTransferEvents(senderTransferReceipt).get(0);

        assertThat(senderTransferEventValues._from,
                equalTo(senderAddress));
        assertThat(senderTransferEventValues._to,
                equalTo(recipientAddress));
        assertThat(senderTransferEventValues._value,
                equalTo(transferQuantity));

        senderQty = senderQty.subtract(transferQuantity);

        BigInteger recipientQty = BigInteger.ZERO;
        recipientQty = recipientQty.add(transferQuantity);

        assertThat(contract.balanceOf(sender.getAddress()).send(),
                equalTo(senderQty));
        assertThat(contract.balanceOf(recipient.getAddress()).send(),
                equalTo(recipientQty));

        // set an allowance
        assertThat(contract.allowance(
                senderAddress, recipientAddress).send(),
                equalTo(BigInteger.ZERO));

        transferQuantity = BigInteger.valueOf(50);
        TransactionReceipt approveReceipt = contract.approve(
                recipient.getAddress(), transferQuantity).send();

        ApprovalEventResponse approvalEventValues =
                contract.getApprovalEvents(approveReceipt).get(0);

        assertThat(approvalEventValues._owner,
                equalTo(senderAddress));
        assertThat(approvalEventValues._spender,
                equalTo(recipientAddress));
        assertThat(approvalEventValues._value,
                equalTo(transferQuantity));

        assertThat(contract.allowance(
                senderAddress, recipientAddress).send(),
                equalTo(transferQuantity));

        // perform a transfer as Bob
        transferQuantity = BigInteger.valueOf(25);

        // Bob requires his own contract instance
        HumanStandardToken recipientsContract = HumanStandardToken.load(
                contract.getContractAddress(), web3j, recipient, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt recipientTransferReceipt = recipientsContract.transferFrom(
                senderAddress,
                recipientAddress,
                transferQuantity).send();

        TransferEventResponse recipientTransferEventValues =
                contract.getTransferEvents(recipientTransferReceipt).get(0);
        assertThat(recipientTransferEventValues._from,
                equalTo(senderAddress));
        assertThat(recipientTransferEventValues._to,
                equalTo(recipientAddress));
        assertThat(recipientTransferEventValues._value,
                equalTo(transferQuantity));

        senderQty = senderQty.subtract(transferQuantity);
        recipientQty = recipientQty.add(transferQuantity);

        assertThat(contract.balanceOf(senderAddress).send(),
                equalTo(senderQty));
        assertThat(contract.balanceOf(recipientAddress).send(),
                equalTo(recipientQty));

        transferEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);
        approvalEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);

        approvalEventSubscription.unsubscribe();
        transferEventSubscription.unsubscribe();
        Thread.sleep(1000);
        assertTrue(approvalEventSubscription.isUnsubscribed());
        assertTrue(transferEventSubscription.isUnsubscribed());
    }

    @Parameterized.Parameters(name = "Test #{index}: {0}")
    public static List<Object[]> parameters() {
        return transferTestParameters();
    }
}
