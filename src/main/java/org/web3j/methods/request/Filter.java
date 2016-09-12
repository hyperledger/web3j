package org.web3j.methods.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter implementation as per <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
public abstract class Filter {
    public List<FilterTopic> topics;

    public Filter() {
        topics = new ArrayList<>();
    }

    public Filter addSingleTopic(String topic) {
        topics.add(new SingleTopic(topic));
        return this;
    }

    public Filter addNullTopic() {
        topics.add(new SingleTopic<>());
        return this;
    }

    // how to pass in null topic?
    public Filter addOptionalTopics(String... optionalTopics) {
        topics.add(new ListTopic(optionalTopics));
        return this;
    }

    public List<FilterTopic> getTopics() {
        return topics;
    }

    public interface FilterTopic<T> {
        T getTopic();
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
        public String getTopic() {
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
        public List<SingleTopic<String>> getTopic() {
            return null;
        }
    }
}
