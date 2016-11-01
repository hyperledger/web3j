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

/**
 * Generated HumanStandardToken integration test for all supported scenarios.
 */
public class HumanStandardTokenGeneratedIT extends Scenario {

    @Test
    public void testContract() throws Exception {
        BigInteger aliceQty = BigInteger.valueOf(1_000_000);
        BigInteger bobQty = BigInteger.ZERO;

        HumanStandardToken contract = HumanStandardToken.deploy(parity, ALICE, BigInteger.ZERO,
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

        EventValues aliceTransferEventValues = contract.processTransferEvent(aliceTransferReceipt);
        assertThat(aliceTransferEventValues.getIndexedValues(),
                equalTo(Arrays.asList(
                        new Address(ALICE.getAddress()),
                        new Address(BOB.getAddress()))));
        assertThat(aliceTransferEventValues.getNonIndexedValues(),
                equalTo(Collections.singletonList(
                        new Uint256(transferQuantity))));

        // set an allowance
        assertThat(contract.allowance(
                new Address(ALICE.getAddress()), new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(BigInteger.ZERO)));

        transferQuantity = BigInteger.valueOf(50);
        TransactionReceipt approveReceipt = contract.approve(new Address(BOB.getAddress()), new Uint256(transferQuantity)).get();

        EventValues approvalEventValues = contract.processApprovalEvent(approveReceipt);
        assertThat(approvalEventValues.getIndexedValues(),
                equalTo(Arrays.asList(
                        new Address(ALICE.getAddress()),
                        new Address(BOB.getAddress()))));
        assertThat(approvalEventValues.getNonIndexedValues(),
                equalTo(Collections.singletonList(
                        new Uint256(transferQuantity))));

        assertThat(contract.allowance(
                new Address(ALICE.getAddress()), new Address(BOB.getAddress())).get(),
                equalTo(new Uint256(transferQuantity)));

        // perform a transfer as Bob
        transferQuantity = BigInteger.valueOf(25);

        // Bob requires his own contract instance
        HumanStandardToken bobsContract =
                new HumanStandardToken(contract.getContractAddress(), parity, BOB);

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

        EventValues bobTransferEventValues = contract.processTransferEvent(bobTransferReceipt);
        assertThat(bobTransferEventValues.getIndexedValues(),
                equalTo(Arrays.asList(
                        new Address(ALICE.getAddress()),
                        new Address(BOB.getAddress()))));
        assertThat(bobTransferEventValues.getNonIndexedValues(),
                equalTo(Collections.singletonList(
                        new Uint256(transferQuantity))));
    }
}
