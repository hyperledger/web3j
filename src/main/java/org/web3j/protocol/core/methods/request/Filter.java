package org.web3j.protocol.core.methods.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Filter implementation as per <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Filter<T extends Filter> {

    private T thisObj;
    private List<FilterTopic> topics;

    Filter() {
        thisObj = getThis();
        topics = new ArrayList<>();
    }

    public T addSingleTopic(String topic) {
        topics.add(new SingleTopic<>(topic));
        return getThis();
    }

    public T addNullTopic() {
        topics.add(new SingleTopic<>());
        return getThis();
    }

    // how to pass in null topic?
    public T addOptionalTopics(String... optionalTopics) {
        topics.add(new ListTopic(optionalTopics));
        return getThis();
    }

    public List<FilterTopic> getTopics() {
        return topics;
    }

    abstract <T extends Filter> T getThis();

    public interface FilterTopic<T> {
        @JsonValue
        T getValue();
    }

    public static class SingleTopic<String> implements FilterTopic {
        private String topic;

        public SingleTopic() {
            this.topic = null;  // null topic
        }

        public SingleTopic(String topic) {
            this.topic = topic;
        }

        @Override
        public String getValue() {
            return topic;
        }
    }

    public static class ListTopic implements FilterTopic<List<SingleTopic<String>>> {
        private List<SingleTopic<String>> topics;

        public ListTopic(String... optionalTopics) {
            topics = new ArrayList<>();
            for (String topic : optionalTopics) {
                if (topic != null) {
                    topics.add(new SingleTopic<>(topic));
                } else {
                    topics.add(new SingleTopic<>());
                }
            }
        }

        @Override
        public List<SingleTopic<String>> getValue() {
            return topics;
        }
    }
}
