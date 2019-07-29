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

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthCall;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReadonlyTransactionManagerTest {

    EthCall response = mock(EthCall.class);
    Request request = mock(Request.class);
    Web3j web3j = mock(Web3j.class);

    @Test
    public void sendCallTest() throws IOException {
        when(response.getValue()).thenReturn("test");
        when(request.send()).thenReturn(response);
        when(web3j.ethCall(any(), any())).thenReturn(request);
        ReadonlyTransactionManager readonlyTransactionManager =
                new ReadonlyTransactionManager(web3j, "");
        String value =
                readonlyTransactionManager.sendCall("", "", DefaultBlockParameterName.LATEST);
        assertThat(value, is("test"));
    }
}
