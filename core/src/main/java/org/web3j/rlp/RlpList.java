package org.web3j.rlp;

import java.util.Arrays;
import java.util.List;

/**
 * RLP list type.
 */
public class RlpList implements RlpType {
    private final List<RlpType> values;

    public RlpList(RlpType... values) {
        this.values = Arrays.asList(values);
    }

    public RlpList(List<RlpType> values) {
        this.values = values;
    }

    public List<RlpType> getValues() {
        return values;
    }
}
