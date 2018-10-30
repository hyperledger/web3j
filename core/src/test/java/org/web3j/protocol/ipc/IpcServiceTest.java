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
        ipcService = new IpcService() {
            @Override
            protected IOFacade getIO() {
                return ioFacade;
            }
        };
    }

    @Test
    public void testSend() throws IOException {
        when(ioFacade.read()).thenReturn(
                "{\"jsonrpc\":\"2.0\",\"id\":1,"
                        + "\"result\":\"Geth/v1.5.4-stable-b70acf3c/darwin/go1.7.3\"}\n");

        ipcService.send(new Request(), Web3ClientVersion.class);

        verify(ioFacade).write("{\"jsonrpc\":\"2.0\",\"method\":null,\"params\":null,\"id\":0}");
    }
}
