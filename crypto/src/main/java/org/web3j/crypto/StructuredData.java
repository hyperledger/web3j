package org.web3j.crypto;

import java.util.HashMap;
import java.util.Vector;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

public class StructuredData {
    static class Entry {
        public String name;
        public String type;

        public Entry() {
        }
    }

    static class EIP712Domain {
        public String name;
        public String version;
        public Uint256 chainId;
        public Address verifyingContract;

        public EIP712Domain() {
        }
    }

    static class EIP712Message {
        public HashMap<String, Vector<Entry>> types;
        public String primaryType;
        public Object message;
        public EIP712Domain domain;

        public EIP712Message() {
        }

        @Override
        public String toString() {
            return "EIP712Message{"
                    + "primaryType='" + this.primaryType + '\''
                    + ", message='" + this.message + '\''
                    + '}';
        }
    }
}