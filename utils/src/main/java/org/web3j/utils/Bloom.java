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
package org.web3j.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.web3j.crypto.Hash;

/**
 * Ethereum Bloom filter. can be used to create a filter or test an item (topic) for a given filter.
 *
 * @author Mehrdad Salehi
 */
public class Bloom {
    // implemented as Ethereum yellow paper, section 4.3.1
    private static final int BYTES_LENGTH = 256;
    private final byte[] bytes = new byte[BYTES_LENGTH];

    /**
     * test topics against a bloom filter.
     *
     * @param bloomBytes the filter bytes.
     * @param topics topics to be tested
     * @return true if all topics is present in filter, otherwise returns false
     * @throws IllegalArgumentException if bloomBytes length is not 256, or it is null, or topics is
     *     null.
     */
    public static boolean test(byte[] bloomBytes, byte[]... topics) {
        Bloom bloom = new Bloom(bloomBytes);
        if (topics == null) {
            throw new IllegalArgumentException("topics can not be null");
        }
        for (byte[] topic : topics) {
            if (!bloom.test(topic)) {
                return false;
            }
        }
        return true;
    }

    /**
     * test topics against a bloom filter.
     *
     * @param bloomBytes the filter bytes.
     * @param topics topics to be tested
     * @return true if all topics is present in filter, otherwise returns false
     * @throws IllegalArgumentException if bloomBytes length is not 256, or it is null, or topics is
     *     null.
     */
    public static boolean test(String bloomBytes, String... topics) {
        Bloom bloom = new Bloom(bloomBytes);
        if (topics == null) {
            throw new IllegalArgumentException("topics can not be null");
        }
        for (String topic : topics) {
            if (!bloom.test(topic)) {
                return false;
            }
        }
        return true;
    }

    /** creates empty filter (all bits set to zero). */
    public Bloom() {}

    /**
     * create filter from hex string.
     *
     * @param bytes the filter data. its length must be 256 bytes.
     * @throws IllegalArgumentException if bytes length is not 256, or it is null.
     */
    public Bloom(String bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can not be null");
        }
        setBytes(Numeric.hexStringToByteArray(bytes));
    }

    /**
     * create filter from bytes.
     *
     * @param bytes the filter data. its length must be 256 bytes.
     * @throws IllegalArgumentException if bytes length is not 256, or it is null.
     */
    public Bloom(byte[] bytes) {
        setBytes(bytes);
    }

    /**
     * add a byte array (topic) to filter. after adding, test() will return true for this topic.
     *
     * @param topic the topic hex string.
     * @throws IllegalArgumentException if topic is null.
     */
    public void add(String topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic can not be null");
        }
        add(Numeric.hexStringToByteArray(topic));
    }

    /**
     * add a byte array (topic) to filter. after adding, test() will return true for this topic.
     *
     * @param topic the topic hex string.
     * @throws IllegalArgumentException if topic is null.
     */
    public void add(byte[] topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic can not be null");
        }
        BloomValues b = getBloomValues(topic);
        this.bytes[b.index[0]] |= b.value[0];
        this.bytes[b.index[1]] |= b.value[1];
        this.bytes[b.index[2]] |= b.value[2];
    }

    /**
     * test presents of a topic. for every topic added by add() this returns true.
     *
     * @param topic the topic hex string.
     * @return true if topic is present (false-positive is possible), and false if topic is not
     *     present(false-negative is not possible).
     * @throws IllegalArgumentException if topic is null.
     */
    public boolean test(String topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic can not be null");
        }
        return test(Numeric.hexStringToByteArray(topic));
    }

    /**
     * test presents of a topic. for every topic added by add() this returns true.
     *
     * @param topic the topic bytes.
     * @return true if topic is present (false-positive is possible), and false if topic is not
     *     present(false-negative is not possible).
     * @throws IllegalArgumentException if topic is null.
     */
    public boolean test(byte[] topic) {
        BloomValues b = getBloomValues(topic);
        return b.value[0] == (b.value[0] & this.bytes[b.index[0]])
                && b.value[1] == (b.value[1] & this.bytes[b.index[1]])
                && b.value[2] == (b.value[2] & this.bytes[b.index[2]]);
    }

    @Override
    public String toString() {
        return getBytesHexString();
    }

    /**
     * @return Bloom filter bytes as hex string
     */
    public String getBytesHexString() {
        return Numeric.toHexString(this.bytes);
    }

    /**
     * @return Bloom filter bytes (returns a copy)
     */
    public byte[] getBytes() {
        byte[] bytesCopy = new byte[BYTES_LENGTH];
        System.arraycopy(this.bytes, 0, bytesCopy, 0, BYTES_LENGTH);
        return bytesCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bloom bloom = (Bloom) o;
        return Arrays.equals(bytes, bloom.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    private void setBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes can not be null");
        }
        if (bytes.length != BYTES_LENGTH) {
            throw new IllegalArgumentException("bytes must be 256 in length");
        }
        System.arraycopy(bytes, 0, this.bytes, 0, BYTES_LENGTH);
    }

    private BloomValues getBloomValues(byte[] item) {
        final byte[] hash = Hash.sha3(item);
        byte v1 = (byte) (1 << (hash[1] & 0x7));
        byte v2 = (byte) (1 << (hash[3] & 0x7));
        byte v3 = (byte) (1 << (hash[5] & 0x7));
        ByteBuffer byteBuffer = ByteBuffer.wrap(hash).order(ByteOrder.BIG_ENDIAN);
        int i1 = BYTES_LENGTH - ((byteBuffer.getShort(0) & 0x7ff) >> 3) - 1;
        int i2 = BYTES_LENGTH - ((byteBuffer.getShort(2) & 0x7ff) >> 3) - 1;
        int i3 = BYTES_LENGTH - ((byteBuffer.getShort(4) & 0x7ff) >> 3) - 1;
        return new BloomValues(new byte[] {v1, v2, v3}, new int[] {i1, i2, i3});
    }

    private record BloomValues(byte[] value, int[] index) {}
}
