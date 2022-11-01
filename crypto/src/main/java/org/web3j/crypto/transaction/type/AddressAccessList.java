/*
 * Copyright 2022 Web3 Labs Ltd.
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

public class AddressAccessList {
    private final String address;

    private final List<BigInteger> slots;

    public AddressAccessList(String address, List<BigInteger> slots) {
        this.address = address;
        this.slots = slots;
    }

    public String getAddress() {
        return address;
    }

    public List<BigInteger> getSlots() {
        return slots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressAccessList that = (AddressAccessList) o;
        return Objects.equals(address, that.address) && Objects.equals(slots, that.slots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, slots);
    }

    public static RlpList getRlpEncodedList(List<AddressAccessList> accessList) {
        List<RlpType> accessListRlp = new ArrayList<>(accessList.size());
        for (int i = 0; i < accessList.size(); i++) {
            AddressAccessList access = accessList.get(i);

            byte[] addressBytes = Numeric.hexStringToByteArray(access.getAddress());
            List<RlpType> slotsRlp = new ArrayList<>(access.getSlots().size());
            for (int j = 0; j < access.getSlots().size(); j++) {
                String slot = Numeric.toHexStringWithPrefixZeroPadded(access.getSlots().get(j), 64);
                slotsRlp.add(RlpString.create(Numeric.hexStringToByteArray(slot)));
            }

            accessListRlp.add(new RlpList(RlpString.create(addressBytes), new RlpList(slotsRlp)));
        }

        return new RlpList(accessListRlp);
    }

    public static List<AddressAccessList> getRlpDecodedList(RlpList encoded) {
        List<AddressAccessList> ret = new ArrayList<>(encoded.size());
        for (int i = 0; i < encoded.size(); i++) {
            RlpList addressSlotsEntry = (RlpList) encoded.getValue(i);

            String address = ((RlpString) addressSlotsEntry.getValue(0)).asString();
            RlpList slotsRlp = (RlpList) addressSlotsEntry.getValue(1);
            List<BigInteger> slots = new ArrayList<>(slotsRlp.size());
            for (int j = 0; j < slotsRlp.size(); j++) {
                slots.add(((RlpString) slotsRlp.getValue(j)).asPositiveBigInteger());
            }

            ret.add(new AddressAccessList(address, slots));
        }
        return ret;
    }
}
