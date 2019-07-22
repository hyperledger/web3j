/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

/** PrivateTransactionManager implementation for using a Quorum node to transact. */
public class EeaTransactionManager extends PrivateTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(EeaTransactionManager.class);

    private static final int SLEEP_DURATION = 1000;
    private static final int ATTEMPTS = 20;

    private final Eea eea;
    private final Credentials credentials;
    private final long chainId;
    private final String privateFrom;
    private List<String> privateFor;
    private String privacyGroupId;

    public EeaTransactionManager(
            final Eea eea,
            final Credentials credentials,
            final long chainId,
            final String privateFrom,
            final List<String> privateFor,
            final int attempts,
            final int sleepDuration) {
        super(eea, privateFrom, attempts, sleepDuration, credentials.getAddress());
        this.eea = eea;
        this.credentials = credentials;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = generatePrivacyGroupId(privateFrom, privateFor);
    }

    public EeaTransactionManager(
            final Eea eea,
            final Credentials credentials,
            final long chainId,
            final String privateFrom,
            final List<String> privateFor) {
        this(eea, credentials, chainId, privateFrom, privateFor, ATTEMPTS, SLEEP_DURATION);
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public void setPrivateFor(final List<String> privateFor) {
        this.privateFor = privateFor;
        this.privacyGroupId = generatePrivacyGroupId(privateFrom, privateFor);
    }

    @Override
    public EthSendTransaction sendTransaction(
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final BigInteger value)
            throws IOException {

        final BigInteger nonce =
                eea.eeaGetTransactionCount(credentials.getAddress(), privacyGroupId)
                        .send()
                        .getTransactionCount();

        final RawPrivateTransaction transaction =
                RawPrivateTransaction.createTransaction(
                        nonce, gasPrice, gasLimit, to, data, privateFrom, privateFor, "restricted");

        final String rawSignedTransaction =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(transaction, chainId, credentials));

        return eea.eeaSendRawTransaction(rawSignedTransaction).send();
    }

    @Override
    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        try {
            return executeCall(to, data);
        } catch (TransactionException e) {
            log.error("Failed to execute call", e);
            return null;
        }
    }

    private String generatePrivacyGroupId(final String privateFrom, final List<String> privateFor) {
        final List<byte[]> stringList = new ArrayList<>();
        stringList.add(Base64.getDecoder().decode(privateFrom));
        privateFor.forEach(item -> stringList.add(Base64.getDecoder().decode(item)));

        final List<RlpType> rlpList =
                stringList.stream()
                        .distinct()
                        .sorted(Comparator.comparing(Arrays::hashCode))
                        .map(RlpString::create)
                        .collect(Collectors.toList());

        return Numeric.toHexString(
                Base64.getEncoder().encode(Hash.sha3(RlpEncoder.encode(new RlpList(rlpList)))));
    }
}
