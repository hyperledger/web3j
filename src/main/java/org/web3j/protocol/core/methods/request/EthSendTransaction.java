package org.web3j.protocol.core.methods.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.utils.Numeric;

/**
 * eth_sendTransaction object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthSendTransaction {
    // default as per https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction
    private static final BigInteger DEFAULT_GAS = BigInteger.valueOf(9000);

    private String from;
    private String to;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;
    private BigInteger nonce;

    public EthSendTransaction(String from, String data) {
        this.from = from;
        this.data = data;
        this.gas = DEFAULT_GAS;
    }

    public EthSendTransaction(String from, BigInteger gas, String data) {
        this.from = from;
        this.gas = gas;
        this.data = data;
    }

    public EthSendTransaction(String from, String to, String data) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.gas = DEFAULT_GAS;
    }

    public EthSendTransaction(String from, String to, BigInteger gas, String data) {
        this.from = from;
        this.to = to;
        this.gas = gas;
        this.data = data;
    }

    public EthSendTransaction(String from, String to,
                              BigInteger gas, BigInteger gasPrice,
                              BigInteger value, String data, BigInteger nonce) {
        this.from = from;
        this.to = to;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getValue() {
        return convert(value);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return convert(nonce);
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
