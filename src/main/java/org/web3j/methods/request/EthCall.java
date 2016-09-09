package org.web3j.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

import org.web3j.protocol.Utils;

/**
 * eth_sendTransaction object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthCall {
    private String fromAddress;
    private String toAddress;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;

    public EthCall(String fromAddress, String data) {
        this.fromAddress = fromAddress;
        this.data = data;
    }

    public EthCall(String fromAddress, String toAddress,
                   BigInteger gas, BigInteger gasPrice,
                   BigInteger value, String data) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.value = value;
        this.data = data;
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

    private static String convert(BigInteger value) {
        if (value != null) {
            return Utils.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
