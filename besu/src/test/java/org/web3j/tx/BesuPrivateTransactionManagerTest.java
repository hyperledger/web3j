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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.utils.Base64String;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.tx.TransactionManager.REVERT_ERR_STR;

class BesuPrivateTransactionManagerTest {

    private static final String OWNER_REVERT_MSG_STR =
            "Only the contract owner can perform this action";
    private static final BesuPrivacyGasProvider ZERO_GAS_PROVIDER =
            new BesuPrivacyGasProvider(BigInteger.valueOf(0));
    private static final long CHAIN_ID = 2018;
    private static final Base64String PRIVATE_FROM =
            Base64String.wrap("GGilEkXLaQ9yhhtbpBT03Me9iYa7U/mWXxrJhnbl1XY=");
    private static final Base64String PRIVACY_GROUP_ID =
            Base64String.wrap("OGD/4dkDZWb4VqgDfElovjYMDAcSiRUiB6fLtFRmugU=");

    Credentials credentials = mock(Credentials.class);
    Web3jService service = mock(Web3jService.class);
    Besu besu = Besu.build(service);
    DefaultBlockParameter defaultBlockParameter = mock(DefaultBlockParameter.class);
    EthCall response = mock(EthCall.class);

    @Test
    public void sendPrivCallTest() throws IOException {
        when(response.getValue()).thenReturn("test");
        when(service.send(any(), any())).thenReturn(response);

        BesuPrivateTransactionManager besuTransactionManager =
                new BesuPrivateTransactionManager(
                        besu,
                        ZERO_GAS_PROVIDER,
                        credentials,
                        CHAIN_ID,
                        PRIVATE_FROM,
                        PRIVACY_GROUP_ID);

        String value = besuTransactionManager.sendCall("", "", defaultBlockParameter);
        assertEquals("test", value);
    }

    @Test
    public void sendPrivCallRevertedTest() throws IOException {
        when(response.isReverted()).thenReturn(true);
        when(response.getRevertReason()).thenReturn(OWNER_REVERT_MSG_STR);
        when(service.send(any(), any())).thenReturn(response);

        BesuPrivateTransactionManager besuTransactionManager =
                new BesuPrivateTransactionManager(
                        besu,
                        ZERO_GAS_PROVIDER,
                        credentials,
                        CHAIN_ID,
                        PRIVATE_FROM,
                        PRIVACY_GROUP_ID);

        ContractCallException thrown =
                assertThrows(
                        ContractCallException.class,
                        () -> besuTransactionManager.sendCall("", "", defaultBlockParameter));
        assertEquals(String.format(REVERT_ERR_STR, OWNER_REVERT_MSG_STR), thrown.getMessage());
    }
}
