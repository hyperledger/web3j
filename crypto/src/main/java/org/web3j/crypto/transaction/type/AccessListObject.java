/*
 * Copyright 2021 Web3 Labs Ltd.
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
package org.web3j.crypto.transaction.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

public class AccessListObject {
    private String address;
    private List<String> storageKeys;

    public AccessListObject() {}

    public AccessListObject(String address, List<String> storageKeys) {
        this.address = address;
        this.storageKeys = storageKeys;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getStorageKeys() {
        return storageKeys;
    }

    public void setStorageKeys(List<String> storageKeys) {
        this.storageKeys = storageKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessListObject that = (AccessListObject) o;
        return Objects.equals(getAddress(), that.getAddress())
                && Objects.equals(getStorageKeys(), that.getStorageKeys());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getStorageKeys());
    }

    public static RlpList getRlpEncodedList(List<AccessListObject> accessList) {
        List<RlpType> accessListRlp = new ArrayList<>(accessList.size());
        for (int i = 0; i < accessList.size(); i++) {
            AccessListObject access = accessList.get(i);

            byte[] addressBytes = Numeric.hexStringToByteArray(access.getAddress());
            List<RlpType> slotsRlp = new ArrayList<>(access.getStorageKeys().size());
            for (int j = 0; j < access.getStorageKeys().size(); j++) {
                slotsRlp.add(
                        RlpString.create(
                                Numeric.hexStringToByteArray(access.getStorageKeys().get(j))));
            }

            accessListRlp.add(new RlpList(RlpString.create(addressBytes), new RlpList(slotsRlp)));
        }

        return new RlpList(accessListRlp);
    }

    public static List<AccessListObject> getRlpDecodedList(RlpList encoded) {
        List<AccessListObject> ret = new ArrayList<>(encoded.size());
        for (int i = 0; i < encoded.size(); i++) {
            RlpList addressSlotsEntry = (RlpList) encoded.getValue(i);

            String address = ((RlpString) addressSlotsEntry.getValue(0)).asString();
            RlpList slotsRlp = (RlpList) addressSlotsEntry.getValue(1);
            List<String> slots = new ArrayList<>(slotsRlp.size());
            for (int j = 0; j < slotsRlp.size(); j++) {
                slots.add(((RlpString) slotsRlp.getValue(j)).asString());
            }

            ret.add(new AccessListObject(address, slots));
        }
        return ret;
    }
}
