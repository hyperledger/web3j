package org.web3j.protocol.core.methods.request;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.utils.Numeric;

/**
 * https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_post
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShhPost {
    private String from;
    private String to;
    private List<String> topics;
    private String payload;
    private BigInteger priority;
    private BigInteger ttl;

    public ShhPost(List<String> topics, String payload, BigInteger priority,
                   BigInteger ttl) {
        this.topics = topics;
        this.payload = payload;
        this.priority = priority;
        this.ttl = ttl;
    }

    public ShhPost(String from, String to, List<String> topics, String payload,
                   BigInteger priority, BigInteger ttl) {
        this.from = from;
        this.to = to;
        this.topics = topics;
        this.payload = payload;
        this.priority = priority;
        this.ttl = ttl;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getPayload() {
        return payload;
    }

    public String getPriority() {
        return convert(priority);
    }

    public String getTtl() {
        return convert(ttl);
    }

    private String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;
        }
    }
}
