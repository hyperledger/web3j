package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.generated.HumanStandardToken;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.web3j.generated.HumanStandardToken.*;

/**
 * Generated HumanStandardToken integration test for all supported scenarios.
 */
public class HumanStandardTokenGeneratedIT extends Scenario {

    @Test
    public void testContract() throws Exception {
        BigInteger aliceQty = BigInteger.valueOf(1_000_000);
        BigInteger bobQty = BigInteger.ZERO;

        HumanStandardToken contract = deploy(parity, ALICE,
                GAS_PRICE, GAS_LIMIT,
                BigInteger.ZERO,
                new Uint256(aliceQty), new Utf8String("web3j tokens"),
                new Uint8(BigInteger.TEN), new Utf8String("w3j$")).get();

        assertThat(contract.totalSupply().get(), equalTo(new Uint256(aliceQty)));

        assertThat(contract.balanceOf(new Address(ALICE.getAddress())).get(),
                equalTo(new Uint256(aliceQty)));

        // transfer tokens
        BigInteger transferQuantity = BigInteger.valueOf(100_000);

        TransactionReceipt aliceTransferReceipt = contract.transfer(new Address(BOB.getAddress()), new Uint256(transferQuantity)).get();

        aliceQty = aliceQty.subtract(transferQuantity);
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(new Address(ALICE.getAddress())).get(),
                equalTo(new Uint256(aliceQty)));
        assertThat(contract.balanceOf(new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(bobQty)));

        TransferEventResponse aliceTransferEventValues = contract.getTransferEvents(aliceTransferReceipt).get(0);
        assertThat(aliceTransferEventValues._from,
                equalTo(ALICE.getAddress()));
        assertThat(aliceTransferEventValues._to,
                equalTo(BOB.getAddress()));
        assertThat(aliceTransferEventValues._value,
                equalTo(new Uint256(transferQuantity)));

        // set an allowance
        assertThat(contract.allowance(
                new Address(ALICE.getAddress()), new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(BigInteger.ZERO)));

        transferQuantity = BigInteger.valueOf(50);
        TransactionReceipt approveReceipt = contract.approve(new Address(BOB.getAddress()), new Uint256(transferQuantity)).get();

        ApprovalEventResponse approvalEventValues = contract.getApprovalEvents(approveReceipt).get(0);
        assertThat(approvalEventValues._owner,
                equalTo(ALICE.getAddress()));
        assertThat(approvalEventValues._spender,
                equalTo(BOB.getAddress()));
        assertThat(approvalEventValues._value,
                equalTo(new Uint256(transferQuantity)));

        assertThat(contract.allowance(
                new Address(ALICE.getAddress()), new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(transferQuantity)));

        // perform a transfer as Bob
        transferQuantity = BigInteger.valueOf(25);

        // Bob requires his own contract instance
        HumanStandardToken bobsContract = HumanStandardToken.load(
                contract.getContractAddress(), parity, BOB, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt bobTransferReceipt = bobsContract.transferFrom(
                new Address(ALICE.getAddress()),
                new Address(BOB.getAddress()),
                new Uint256(transferQuantity)).get();

        aliceQty = aliceQty.subtract(transferQuantity);
        bobQty = bobQty.add(transferQuantity);

        assertThat(contract.balanceOf(new Address(ALICE.getAddress())).get(),
                equalTo(new Uint256(aliceQty)));
        assertThat(contract.balanceOf(new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(bobQty)));

        TransferEventResponse bobTransferEventValues = contract.getTransferEvents(bobTransferReceipt).get(0);
        assertThat(bobTransferEventValues._from,
                equalTo(ALICE.getAddress()));
        assertThat(bobTransferEventValues._to,
                equalTo(BOB.getAddress()));
        assertThat(bobTransferEventValues._value,
                equalTo(new Uint256(transferQuantity)));
    }
}
