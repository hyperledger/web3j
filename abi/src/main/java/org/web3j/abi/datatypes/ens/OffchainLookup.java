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
package org.web3j.abi.datatypes.ens;

import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes4;

/** https://eips.ethereum.org/EIPS/eip-3668#client-lookup-protocol */
public class OffchainLookup extends DynamicStruct {

    public String sender;
    public List<String> urls;
    public byte[] callData;
    public byte[] callbackFunction;
    public byte[] extraData;

    public OffchainLookup(
            String sender,
            List<String> urls,
            byte[] callData,
            byte[] callbackFunction,
            byte[] extraData) {
        super(
                new Address(sender),
                new DynamicArray<>(
                        Utf8String.class,
                        urls.stream().map(Utf8String::new).collect(Collectors.toList())),
                new Bytes4(callData),
                new DynamicBytes(callbackFunction),
                new DynamicBytes(extraData));
        this.sender = sender;
        this.urls = urls;
        this.callData = callData;
        this.callbackFunction = callbackFunction;
        this.extraData = extraData;
    }

    public OffchainLookup(
            Address sender,
            DynamicArray<Utf8String> urls,
            Bytes4 callData,
            DynamicBytes callbackFunction,
            DynamicBytes extraData) {
        super(sender, urls, callData, callbackFunction, extraData);
        this.sender = sender.getValue();
        this.urls = urls.getValue().stream().map(Utf8String::getValue).collect(Collectors.toList());
        this.callData = callData.getValue();
        this.callbackFunction = callbackFunction.getValue();
        this.extraData = extraData.getValue();
    }
}
