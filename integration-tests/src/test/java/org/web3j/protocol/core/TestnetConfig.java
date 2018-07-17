package org.web3j.protocol.core;

import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.core.methods.request.Transaction;

/**
 * Mordon Testnet Configuration.
 */
public class TestnetConfig implements IntegrationTestConfig {

    @Override
    public String validBlockHash() {
        // https://ropsten.etherscan.io/block/71032
        return "0xd18e6e9b74b11993242efddd22a458831ac4540d2866d5706af7e624481bb391";
    }

    @Override
    public BigInteger validBlock() {
        // https://testnet.etherscan.io/block/71032
        return BigInteger.valueOf(71032);
    }

    @Override
    public BigInteger validBlockTransactionCount() {
        return BigInteger.valueOf(3);
    }

    @Override
    public BigInteger validBlockUncleCount() {
        return BigInteger.ZERO;
    }

    @Override
    public String validAccount() {
        // https://testnet.etherscan.io/address/0xCB10FBad79F5e602699fFf2Bb4919Fbd87AbC8CC
        return "0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc";
    }

    @Override
    public String validContractAddress() {
        // https://ropsten.etherscan.io/address/0x479cc461fecd078f766ecc58533d6f69580cf3ac
        return "0x479CC461fEcd078F766eCc58533D6F69580CF3AC";
    }

    @Override
    public String validContractAddressPositionZero() {
        return "0x000000000000000000000000a8e9fa8f91e5ae138c74648c9c304f1c75003a8d";
    }

    @Override
    public String validContractCode() {
        return "0x";
    }

    @Override
    public Transaction buildTransaction() {
        return Transaction.createContractTransaction(
                validAccount(),
                BigInteger.ZERO,  // nonce
                Transaction.DEFAULT_GAS,
                validContractCode()
        );
    }

    @Override
    public String validTransactionHash() {
        // https://ropsten.etherscan.io/tx/0x1f77ebd5f1bea5065027671bc99b6237033f861692c568496d19fd37dddabe55
        return "0x1f77ebd5f1bea5065027671bc99b6237033f861692c568496d19fd37dddabe55";
    }

    @Override
    public String validUncleBlockHash() {
        // https://ropsten.etherscan.io/block/71194
        return "0x3913b9f417047e711ec6dc1e42e7075d924c3ad0d013f74945e5680170cde1e9";
    }

    @Override
    public BigInteger validUncleBlock() {
        // https://ropsten.etherscan.io/block/71194
        return BigInteger.valueOf(71194);
    }

    @Override
    public String encodedEvent() {
        return EventEncoder.buildEventSignature("LogCancel(address,address,address,address,uint256"
            + ",uint256,bytes32,bytes32)");
    }
}
