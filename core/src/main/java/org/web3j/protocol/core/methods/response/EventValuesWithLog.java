package org.web3j.protocol.core.methods.response;

import org.web3j.abi.datatypes.Type;
import org.web3j.abi.EventValues;

import java.util.List;

public class EventValuesWithLog extends EventValues {

    private Log log;

    public EventValuesWithLog(List<Type> indexedValues, List<Type> nonIndexedValues, Log log) {
        super(indexedValues, nonIndexedValues);
        this.log = log;
    }

    public Log getLog() {
        return log;
    }

}
