/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * Whisper messages returned by:
 *
 * <ul>
 *   <li>shh_getFilterChanges
 *   <li>shh_getMessages
 * </ul>
 *
 * <p>See <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_getfilterchanges">docs</a> for
 * further details.
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

        public SshMessage() {}

        public SshMessage(
                final String hash,
                final String from,
                final String to,
                final String expiry,
                final String ttl,
                final String sent,
                final List<String> topics,
                final String payload,
                final String workProved) {
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

        public void setHash(final String hash) {
            this.hash = hash;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(final String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(final String to) {
            this.to = to;
        }

        public BigInteger getExpiry() {
            return Numeric.decodeQuantity(expiry);
        }

        public String getExpiryRaw() {
            return expiry;
        }

        public void setExpiry(final String expiry) {
            this.expiry = expiry;
        }

        public BigInteger getTtl() {
            return Numeric.decodeQuantity(ttl);
        }

        public String getTtlRaw() {
            return ttl;
        }

        public void setTtl(final String ttl) {
            this.ttl = ttl;
        }

        public BigInteger getSent() {
            return Numeric.decodeQuantity(sent);
        }

        public String getSentRaw() {
            return sent;
        }

        public void setSent(final String sent) {
            this.sent = sent;
        }

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(final List<String> topics) {
            this.topics = topics;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(final String payload) {
            this.payload = payload;
        }

        public BigInteger getWorkProved() {
            return Numeric.decodeQuantity(workProved);
        }

        public String getWorkProvedRaw() {
            return workProved;
        }

        public void setWorkProved(final String workProved) {
            this.workProved = workProved;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SshMessage)) {
                return false;
            }

            final SshMessage that = (SshMessage) o;

            if (getHash() != null ? !getHash().equals(that.getHash()) : that.getHash() != null) {
                return false;
            }
            if (getFrom() != null ? !getFrom().equals(that.getFrom()) : that.getFrom() != null) {
                return false;
            }
            if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null) {
                return false;
            }
            if (getExpiryRaw() != null
                    ? !getExpiryRaw().equals(that.getExpiryRaw())
                    : that.getExpiryRaw() != null) {
                return false;
            }
            if (getTtlRaw() != null
                    ? !getTtlRaw().equals(that.getTtlRaw())
                    : that.getTtlRaw() != null) {
                return false;
            }
            if (getSentRaw() != null
                    ? !getSentRaw().equals(that.getSentRaw())
                    : that.getSentRaw() != null) {
                return false;
            }
            if (getTopics() != null
                    ? !getTopics().equals(that.getTopics())
                    : that.getTopics() != null) {
                return false;
            }
            if (getPayload() != null
                    ? !getPayload().equals(that.getPayload())
                    : that.getPayload() != null) {
                return false;
            }
            return getWorkProvedRaw() != null
                    ? getWorkProvedRaw().equals(that.getWorkProvedRaw())
                    : that.getWorkProvedRaw() == null;
        }

        @Override
        public int hashCode() {
            int result = getHash() != null ? getHash().hashCode() : 0;
            result = 31 * result + (getFrom() != null ? getFrom().hashCode() : 0);
            result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
            result = 31 * result + (getExpiryRaw() != null ? getExpiryRaw().hashCode() : 0);
            result = 31 * result + (getTtlRaw() != null ? getTtlRaw().hashCode() : 0);
            result = 31 * result + (getSentRaw() != null ? getSentRaw().hashCode() : 0);
            result = 31 * result + (getTopics() != null ? getTopics().hashCode() : 0);
            result = 31 * result + (getPayload() != null ? getPayload().hashCode() : 0);
            result = 31 * result + (getWorkProvedRaw() != null ? getWorkProvedRaw().hashCode() : 0);
            return result;
        }
    }
}
