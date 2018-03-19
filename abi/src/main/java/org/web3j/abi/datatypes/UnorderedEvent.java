package org.web3j.abi.datatypes;

import org.web3j.abi.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnorderedEvent {
    private class EventType {
        public boolean indexed;
        public TypeReference<Type> type;
        public int seqNum;
    }

    private String name;
    private List<EventType> params;

    public UnorderedEvent(String name) {
        this.name = name;
        this.params = new ArrayList<>();
    }

    public void add(boolean indexed, TypeReference type) {
        EventType eventType = new EventType();
        eventType.indexed = indexed;
        eventType.type = type;
        eventType.seqNum = this.params.size();
        this.params.add(eventType);
    }

    public String getName() {
        return name;
    }

    public List<TypeReference<Type>> getParameters() {
        return this.params.stream().map(eventType -> eventType.type).collect(Collectors.toList());
    }

    public List<TypeReference<Type>> getIndexedParameters() {
        return this.params.stream()
                .filter(eventType -> eventType.indexed)
                .map(eventType -> eventType.type)
                .collect(Collectors.toList());
    }

    public List<Integer> getIndexedParametersSeq() {
        return this.params.stream()
                .filter(eventType -> eventType.indexed)
                .map(eventType -> eventType.seqNum)
                .collect(Collectors.toList());
    }

    public List<TypeReference<Type>> getNonIndexedParameters() {
        return this.params.stream()
                .filter(eventType -> !eventType.indexed)
                .map(eventType -> eventType.type)
                .collect(Collectors.toList());
    }

    public List<Integer> getNonIndexedParametersSeq() {
        return this.params.stream()
                .filter(eventType -> !eventType.indexed)
                .map(eventType -> eventType.seqNum)
                .collect(Collectors.toList());
    }
}
