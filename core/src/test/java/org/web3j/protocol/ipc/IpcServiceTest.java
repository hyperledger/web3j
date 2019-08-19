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
package org.web3j.protocol.ipc;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IpcServiceTest {

    private IpcService ipcService;
    private IOFacade ioFacade;

    @Before
    public void setUp() {
        ioFacade = mock(IOFacade.class);
        ipcService =
                new IpcService() {
                    @Override
                    protected IOFacade getIO() {
                        return ioFacade;
                    }
                };
    }

    @Test
    public void testSend() throws IOException {
        when(ioFacade.read())
                .thenReturn(
                        "{\"jsonrpc\":\"2.0\",\"id\":1,"
                                + "\"result\":\"Geth/v1.5.4-stable-b70acf3c/darwin/go1.7.3\"}\n");

        ipcService.send(new Request(), Web3ClientVersion.class);

        verify(ioFacade).write("{\"jsonrpc\":\"2.0\",\"method\":null,\"params\":null,\"id\":0}");
    }
}
