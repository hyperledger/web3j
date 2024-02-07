/*
 * Copyright 2024 Web3 Labs Ltd.
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
package org.web3j.crypto;

import java.util.Objects;

import org.apache.tuweni.bytes.Bytes;

public class Blob {

    final Bytes data;

    /**
     * Create a new Blob.
     *
     * @param data that represents the blob in Bytes.
     */
    public Blob(final Bytes data) {
        this.data = data;
    }

    /**
     * Create a new Blob.
     *
     * @param data that represents the blob in byte[].
     */
    public Blob(final byte[] data) {
        this.data = Bytes.wrap(data);
    }

    /**
     * Get the data of the Blob.
     *
     * @return the data.
     */
    public Bytes getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blob blob = (Blob) o;
        return Objects.equals(data, blob.data);
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Blob{" + "data=" + data + '}';
    }
}
