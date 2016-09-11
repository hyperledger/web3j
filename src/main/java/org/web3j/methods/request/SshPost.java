package org.web3j.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.web3j.protocol.Utils;

import java.math.BigInteger;
import java.util.List;

/**
 * https://github.com/ethereum/wiki/wiki/JSON-RPC#shh_post
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SshPost {
    private String from;
    private String to;
    private List<String> topics;
    private String payload;
    private BigInteger priority;
    private BigInteger ttl;

    public SshPost(List<String> topics, String payload, BigInteger priority,
                   BigInteger ttl) {
        this.topics = topics;
        this.payload = payload;
        this.priority = priority;
        this.ttl = ttl;
    }

    public SshPost(String from, String to, List<String> topics, String payload,
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
            return Utils.encodeQuantity(value);
        } else {
            return null;
        }
    }
}
