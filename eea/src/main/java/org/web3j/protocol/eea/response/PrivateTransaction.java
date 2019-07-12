package org.web3j.protocol.eea.response;

import java.util.List;
import java.util.Objects;

public class PrivateTransaction {
    private String hash;
    private String nonce;
    private String from;
    private String to;
    private String value;
    private String gasPrice;
    private String gas;
    private String input;
    private String r;
    private String s;
    private long v;
    private String privateFrom;
    private List<String> privateFor;
    private String restriction;

    public PrivateTransaction() {
        super();
    }

    public PrivateTransaction(
            final String hash, final String nonce,
            final String from, final String to,
            final String value, final String gas,
            final String gasPrice, final String input,
            final String r, final String s, final long v,
            final String privateFrom, final List<String> privateFor,
            final String restriction) {

        this.hash = hash;
        this.nonce = nonce;
        this.from = from;
        this.to = to;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gas = gas;
        this.input = input;
        this.r = r;
        this.s = s;
        this.v = v;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.restriction = restriction;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public long getV() {
        return v;
    }

    public void setV(long v) {
        this.v = v;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public void setPrivateFrom(String privateFrom) {
        this.privateFrom = privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public void setPrivateFor(List<String> privateFor) {
        this.privateFor = privateFor;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateTransaction that = (PrivateTransaction) o;
        return v == that.v &&
                hash.equals(that.hash) &&
                nonce.equals(that.nonce) &&
                from.equals(that.from) &&
                Objects.equals(to, that.to) &&
                value.equals(that.value) &&
                gasPrice.equals(that.gasPrice) &&
                gas.equals(that.gas) &&
                input.equals(that.input) &&
                r.equals(that.r) &&
                s.equals(that.s) &&
                privateFrom.equals(that.privateFrom) &&
                privateFor.equals(that.privateFor) &&
                restriction.equals(that.restriction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, nonce, from, to, value, gasPrice, gas, input, r, s, v, privateFrom, privateFor, restriction);
    }
}
