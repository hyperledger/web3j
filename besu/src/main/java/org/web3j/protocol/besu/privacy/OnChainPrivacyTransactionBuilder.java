/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.protocol.besu.privacy;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;
import org.web3j.utils.Restriction;

public class OnChainPrivacyTransactionBuilder {

    private static final BesuPrivacyGasProvider ZERO_GAS_PROVIDER =
            new BesuPrivacyGasProvider(BigInteger.valueOf(0));
    public static final String OnChainPrivacyPrecompiledContract =
            "0x000000000000000000000000000000000000007c";

    private final long chainId;
    private final BesuPrivacyGasProvider gasProvider;
    private final Restriction restriction;

    public OnChainPrivacyTransactionBuilder(
            final long chainId,
            final BesuPrivacyGasProvider gasProvider,
            final Restriction restriction) {
        this.chainId = chainId;
        this.gasProvider = gasProvider;
        this.restriction = restriction;
    }

    public OnChainPrivacyTransactionBuilder() {
        this(2018, ZERO_GAS_PROVIDER, Restriction.RESTRICTED);
    }

    public static String getEncodedRemoveFromGroupFunction(
            Base64String enclaveKey, byte[] participant) {
        final org.web3j.abi.datatypes.Function function =
                new org.web3j.abi.datatypes.Function(
                        "removeParticipant",
                        Arrays.asList(new Bytes32(enclaveKey.raw()), new Bytes32(participant)),
                        Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public static String getEncodedAddToGroupFunction(List<byte[]> participants) {
        final Function function =
                new Function(
                        "addParticipants",
                        Arrays.asList(
                                new DynamicArray<>(
                                        Bytes32.class, Utils.typeMap(participants, Bytes32.class))),
                        Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public static String getEncodedSingleParamFunction(final String functionName) {
        final Function function =
                new Function(functionName, Collections.emptyList(), Collections.emptyList());
        return FunctionEncoder.encode(function);
    }

    public String buildOnChainPrivateTransaction(
            Base64String privacyGroupId,
            Credentials credentials,
            Base64String enclaveKey,
            final BigInteger nonce,
            String call) {
        RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasProvider.getGasPrice(null),
                        gasProvider.getGasLimit(null),
                        OnChainPrivacyPrecompiledContract,
                        call,
                        enclaveKey,
                        privacyGroupId,
                        restriction);

        return Numeric.toHexString(
                PrivateTransactionEncoder.signMessage(rawTransaction, chainId, credentials));
    }
}
