package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.generated.HumanStandardToken;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.generated.HumanStandardToken.ApprovalEventResponse;
import static org.web3j.generated.HumanStandardToken.TransferEventResponse;
import static org.web3j.generated.HumanStandardToken.deploy;

/**
 * Generated HumanStandardToken integration test for all supported scenarios.
 */
public class HumanStandardTokenGeneratedIT extends Scenario {

    @Test
    public void testContract() throws Exception {
        BigInteger aliceQty = BigInteger.valueOf(1_000_000);
        final Address aliceAddress = new Address(ALICE.getAddress());
        final Address bobAddress = new Address(BOB.getAddress());

        HumanStandardToken contract = deploy(parity, ALICE,
                GAS_PRICE, GAS_LIMIT,
                BigInteger.ZERO,
                new Uint256(aliceQty), new Utf8String("web3j tokens"),
                new Uint8(BigInteger.TEN), new Utf8String("w3j$")).send();

        assertTrue(contract.isValid());

        assertThat(contract.totalSupply().send(), equalTo(new Uint256(aliceQty)));

        assertThat(contract.balanceOf(new Address(ALICE.getAddress())).send(),
                equalTo(new Uint256(aliceQty)));

        // transfer tokens
        BigInteger transferQuantity = BigInteger.valueOf(100_000);

        TransactionReceipt aliceTransferReceipt = contract.transfer(
                new Address(BOB.getAddress()), new Uint256(transferQuantity)).send();

        TransferEventResponse aliceTransferEventValues =
                contract.getTransferEvents(aliceTransferReceipt).get(0);

        assertThat(aliceTransferEventValues._from,
                equalTo(aliceAddress));
        assertThat(aliceTransferEventValues._to,
                equalTo(bobAddress));
        assertThat(aliceTransferEventValues._value,
                equalTo(new Uint256(transferQuantity)));

        aliceQty = aliceQty.subtract(transferQuantity);

        BigInteger bobQty = BigInteger.ZERO;
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(new Address(ALICE.getAddress())).send(),
                equalTo(new Uint256(aliceQty)));
        assertThat(contract.balanceOf(new Address(BOB.getAddress())).send(),
                equalTo(new Uint256(bobQty)));

        // set an allowance
        assertThat(contract.allowance(
                aliceAddress, bobAddress).send(),
                equalTo(new Uint256(BigInteger.ZERO)));

        transferQuantity = BigInteger.valueOf(50);
        TransactionReceipt approveReceipt = contract.approve(
                new Address(BOB.getAddress()), new Uint256(transferQuantity)).send();

        ApprovalEventResponse approvalEventValues =
                contract.getApprovalEvents(approveReceipt).get(0);

        assertThat(approvalEventValues._owner,
                equalTo(aliceAddress));
        assertThat(approvalEventValues._spender,
                equalTo(bobAddress));
        assertThat(approvalEventValues._value,
                equalTo(new Uint256(transferQuantity)));

        assertThat(contract.allowance(
                aliceAddress, bobAddress).send(),
                equalTo(new Uint256(transferQuantity)));

        // perform a transfer as Bob
        transferQuantity = BigInteger.valueOf(25);

        // Bob requires his own contract instance
        HumanStandardToken bobsContract = HumanStandardToken.load(
                contract.getContractAddress(), parity, BOB, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt bobTransferReceipt = bobsContract.transferFrom(
                aliceAddress,
                bobAddress,
                new Uint256(transferQuantity)).send();

        TransferEventResponse bobTransferEventValues =
                contract.getTransferEvents(bobTransferReceipt).get(0);
        assertThat(bobTransferEventValues._from,
                equalTo(aliceAddress));
        assertThat(bobTransferEventValues._to,
                equalTo(bobAddress));
        assertThat(bobTransferEventValues._value,
                equalTo(new Uint256(transferQuantity)));

        aliceQty = aliceQty.subtract(transferQuantity);
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(aliceAddress).send(),
                equalTo(new Uint256(aliceQty)));
        assertThat(contract.balanceOf(bobAddress).send(),
                equalTo(new Uint256(bobQty)));
    }
}
