package org.web3j.protocol.eea.response;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = PrivateTransaction.ResponseDeserialiser.class)
public abstract class PrivateTransaction {
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
    private String restriction;

    public PrivateTransaction(
            final String hash, final String nonce,
            final String from, final String to,
            final String value, final String gas,
            final String gasPrice, final String input,
            final String r, final String s, final long v,
            final String privateFrom,
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
        this.restriction = restriction;
    }

    public static class ResponseDeserialiser extends StdDeserializer<PrivateTransaction> {

        protected ResponseDeserialiser() {
            super(PrivateTransaction.class);
        }

        @Override
        public PrivateTransaction deserialize(
                JsonParser p, DeserializationContext ctxt) throws IOException {
            final TreeNode node = p.readValueAsTree();

            // Select the concrete class based on the existence of a property
            if (node.get("privateFor") != null) {
                return p.getCodec().treeToValue(node, PrivateTransactionLegacy.class);
            }
            return p.getCodec().treeToValue(node, PrivateTransactionWithPrivacyGroup.class);
        }
    }

    public String getHash() {
        return hash;
    }

    public String getNonce() {
        return nonce;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getValue() {
        return value;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public String getGas() {
        return gas;
    }

    public String getInput() {
        return input;
    }

    public String getR() {
        return r;
    }

    public String getS() {
        return s;
    }

    public long getV() {
        return v;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public String getRestriction() {
        return restriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivateTransaction that = (PrivateTransaction) o;
        return getV() == that.getV()
                && getHash().equals(that.getHash())
                && getNonce().equals(that.getNonce())
                && getFrom().equals(that.getFrom())
                && getTo().equals(that.getTo())
                && getValue().equals(that.getValue())
                && getGasPrice().equals(that.getGasPrice())
                && getGas().equals(that.getGas())
                && getInput().equals(that.getInput())
                && getR().equals(that.getR())
                && getS().equals(that.getS())
                && getPrivateFrom().equals(that.getPrivateFrom())
                && getRestriction().equals(that.getRestriction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getHash(), getNonce(), getFrom(),
                getTo(), getValue(), getGasPrice(),
                getGas(), getInput(), getR(), getS(),
                getV(), getPrivateFrom(), getRestriction());
    }
}
