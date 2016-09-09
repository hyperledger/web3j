package org.web3j.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.web3j.protocol.Utils;

import java.math.BigInteger;

/**
 * eth_sendTransaction object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthSendTransaction {
    private String fromAddress;
    private String toAddress;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;
    private BigInteger nonce;

    public EthSendTransaction(String fromAddress, String data) {
        this.fromAddress = fromAddress;
        this.data = data;
    }

    public EthSendTransaction(String fromAddress, String toAddress,
                              BigInteger gas, BigInteger gasPrice,
                              BigInteger value, String data, BigInteger nonce) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
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
            return Utils.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
