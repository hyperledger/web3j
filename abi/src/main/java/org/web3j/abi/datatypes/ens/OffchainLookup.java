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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.utils.Numeric;

/** https://eips.ethereum.org/EIPS/eip-3668#client-lookup-protocol */
public class OffchainLookup extends DynamicStruct {

    private String sender;
    private List<String> urls;
    private byte[] callData;
    private byte[] callbackFunction;
    private byte[] extraData;

    private static final List outputParameters = new ArrayList<TypeReference<Type>>();

    static {
        outputParameters.addAll(
                Arrays.asList(
                        new TypeReference<Address>() {},
                        new TypeReference<DynamicArray<Utf8String>>() {},
                        new TypeReference<DynamicBytes>() {},
                        new TypeReference<Bytes4>() {},
                        new TypeReference<DynamicBytes>() {}));
    }

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
                new DynamicBytes(callbackFunction),
                new Bytes4(callData),
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
            DynamicBytes callData,
            Bytes4 callbackFunction,
            DynamicBytes extraData) {
        super(sender, urls, callData, callbackFunction, extraData);
        this.sender = sender.getValue();
        this.urls = urls.getValue().stream().map(Utf8String::getValue).collect(Collectors.toList());
        this.callData = callData.getValue();
        this.callbackFunction = callbackFunction.getValue();
        this.extraData = extraData.getValue();
    }

    public static OffchainLookup build(byte[] bytes) {
        List<Type> resultList =
                FunctionReturnDecoder.decode(Numeric.toHexString(bytes), outputParameters);

        return new OffchainLookup(
                (Address) resultList.get(0),
                (DynamicArray<Utf8String>) resultList.get(1),
                (DynamicBytes) resultList.get(2),
                (Bytes4) resultList.get(3),
                (DynamicBytes) resultList.get(4));
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public byte[] getCallData() {
        return callData;
    }

    public void setCallData(byte[] callData) {
        this.callData = callData;
    }

    public byte[] getCallbackFunction() {
        return callbackFunction;
    }

    public void setCallbackFunction(byte[] callbackFunction) {
        this.callbackFunction = callbackFunction;
    }

    public byte[] getExtraData() {
        return extraData;
    }

    public void setExtraData(byte[] extraData) {
        this.extraData = extraData;
    }
}
