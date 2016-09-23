package org.web3j.protocol.parity.methods.response;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.utils.Codec;

/**
 * personal_requestsToConfirm
 */
public class PersonalRequestsToConfirm extends Response<List<PersonalRequestsToConfirm.RequestsToConfirm>> {

    public List<RequestsToConfirm> getRequestsToConfirm() {
        return getResult();
    }

    public static class RequestsToConfirm {

        private String id;
        private PayloadType payload;

        public RequestsToConfirm() {
        }

        public RequestsToConfirm(String id, PayloadType payload) {
            this.id = id;
            this.payload = payload;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public PayloadType getPayload() {
            return payload;
        }

        @JsonDeserialize(using = ResponseDeserialiser.class)
        public void setPayload(PayloadType  payload) {
            this.payload = payload;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RequestsToConfirm that = (RequestsToConfirm) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            return payload != null ? payload.equals(that.payload) : that.payload == null;

        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (payload != null ? payload.hashCode() : 0);
            return result;
        }
    }

    public interface PayloadType { }

    public static class SignPayload implements PayloadType {
        private SignRequest sign;

        public SignPayload() { }

        public SignPayload(SignRequest sign) {
            this.sign = sign;
        }

        public SignRequest getSign() {
            return sign;
        }

        public void setSign(SignRequest sign) {
            this.sign = sign;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SignPayload signPayload = (SignPayload) o;

            return sign != null ? sign.equals(signPayload.sign) : signPayload.sign == null;
        }

        @Override
        public int hashCode() {
            return sign != null ? sign.hashCode() : 0;
        }
    }

    public static class SignRequest {
        private String address;
        private String hash;

        public SignRequest() {
        }

        public SignRequest(String address, String hash) {
            this.address = address;
            this.hash = hash;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SignRequest that = (SignRequest) o;

            if (address != null ? !address.equals(that.address) : that.address != null)
                return false;
            return hash != null ? hash.equals(that.hash) : that.hash == null;

        }

        @Override
        public int hashCode() {
            int result = address != null ? address.hashCode() : 0;
            result = 31 * result + (hash != null ? hash.hashCode() : 0);
            return result;
        }
    }

    public static class TransactionPayload implements PayloadType {
        private TransactionRequestType transaction;

        public TransactionPayload() { }

        public TransactionPayload(TransactionRequestType transaction) {
            this.transaction = transaction;
        }

        public TransactionRequestType getTransaction() {
            return transaction;
        }

        public void setTransaction(TransactionRequestType transaction) {
            this.transaction = transaction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TransactionPayload that = (TransactionPayload) o;

            return transaction != null ? transaction.equals(that.transaction) : that.transaction == null;
        }

        @Override
        public int hashCode() {
            return transaction != null ? transaction.hashCode() : 0;
        }
    }

    // Basically the same as EthSendTransaction
    public static class TransactionRequestType {
        private String from;
        private String to;
        private String gas;
        private String gasPrice;
        private String value;
        private String data;
        private String nonce;

        public TransactionRequestType() {
        }

        public TransactionRequestType(String from, String data) {
            this.from = from;
            this.data = data;
        }

        public TransactionRequestType(String from, String to,
                                      String gas, String gasPrice,
                                      String value, String data, String nonce) {
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

        public BigInteger getGas() {
            return Codec.decodeQuantity(gas);
        }

        public BigInteger getGasPrice() {
            return Codec.decodeQuantity(gasPrice);
        }

        public BigInteger getValue() {
            return Codec.decodeQuantity(value);
        }

        public String getData() {
            return data;
        }

        public BigInteger getNonce() {
            return Codec.decodeQuantity(nonce);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TransactionRequestType that = (TransactionRequestType) o;

            if (from != null ? !from.equals(that.from) : that.from != null) return false;
            if (to != null ? !to.equals(that.to) : that.to != null) return false;
            if (gas != null ? !gas.equals(that.gas) : that.gas != null) return false;
            if (gasPrice != null ? !gasPrice.equals(that.gasPrice) : that.gasPrice != null)
                return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;
            if (data != null ? !data.equals(that.data) : that.data != null) return false;
            return nonce != null ? nonce.equals(that.nonce) : that.nonce == null;

        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            result = 31 * result + (gas != null ? gas.hashCode() : 0);
            result = 31 * result + (gasPrice != null ? gasPrice.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            result = 31 * result + (data != null ? data.hashCode() : 0);
            result = 31 * result + (nonce != null ? nonce.hashCode() : 0);
            return result;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<PayloadType> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public PayloadType deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
                jsonParser.nextToken();
                String value = jsonParser.getValueAsString();
                if (value.equals("sign")) {
                    return objectReader.readValue(jsonParser, SignPayload.class);
                } else {
                    return objectReader.readValue(jsonParser, TransactionPayload.class);
                }
            }
            return null;
        }
    }

}
