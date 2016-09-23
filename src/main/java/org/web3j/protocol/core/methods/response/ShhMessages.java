package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * <p>Whisper messages returned by:
 * <ul>
 * <li>shh_getFilterChanges</li>
 * <li>shh_getMessages</li>
 * </ul>
 * </p>
 * <p>
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getfilterchanges">docs</a>
 * for further details.</p>
 */
public class ShhMessages extends Response<List<ShhMessages.SshMessage>> {

    public List<SshMessage> getMessages() {
        return getResult();
    }

    public static class SshMessage {
        private String hash;
        private String from;
        private String to;
        private String expiry;
        private String ttl;
        private String sent;
        private List<String> topics;
        private String payload;
        private String workProved;

        public SshMessage() {
        }

        public SshMessage(String hash, String from, String to, String expiry, String ttl,
                          String sent, List<String> topics, String payload, String workProved) {
            this.hash = hash;
            this.from = from;
            this.to = to;
            this.expiry = expiry;
            this.ttl = ttl;
            this.sent = sent;
            this.topics = topics;
            this.payload = payload;
            this.workProved = workProved;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
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

        public BigInteger getExpiry() {
            return Codec.decodeQuantity(expiry);
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public BigInteger getTtl() {
            return Codec.decodeQuantity(ttl);
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

        public BigInteger getSent() {
            return Codec.decodeQuantity(sent);
        }

        public void setSent(String sent) {
            this.sent = sent;
        }

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public BigInteger getWorkProved() {
            return Codec.decodeQuantity(workProved);
        }

        public void setWorkProved(String workProved) {
            this.workProved = workProved;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SshMessage that = (SshMessage) o;

            if (hash != null ? !hash.equals(that.hash) : that.hash != null) return false;
            if (from != null ? !from.equals(that.from) : that.from != null) return false;
            if (to != null ? !to.equals(that.to) : that.to != null) return false;
            if (expiry != null ? !expiry.equals(that.expiry) : that.expiry != null) return false;
            if (ttl != null ? !ttl.equals(that.ttl) : that.ttl != null) return false;
            if (sent != null ? !sent.equals(that.sent) : that.sent != null) return false;
            if (topics != null ? !topics.equals(that.topics) : that.topics != null) return false;
            if (payload != null ? !payload.equals(that.payload) : that.payload != null)
                return false;
            return workProved != null ? workProved.equals(that.workProved) : that.workProved == null;

        }

        @Override
        public int hashCode() {
            int result = hash != null ? hash.hashCode() : 0;
            result = 31 * result + (from != null ? from.hashCode() : 0);
            result = 31 * result + (to != null ? to.hashCode() : 0);
            result = 31 * result + (expiry != null ? expiry.hashCode() : 0);
            result = 31 * result + (ttl != null ? ttl.hashCode() : 0);
            result = 31 * result + (sent != null ? sent.hashCode() : 0);
            result = 31 * result + (topics != null ? topics.hashCode() : 0);
            result = 31 * result + (payload != null ? payload.hashCode() : 0);
            result = 31 * result + (workProved != null ? workProved.hashCode() : 0);
            return result;
        }
    }
}
