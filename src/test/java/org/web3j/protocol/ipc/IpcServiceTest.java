package org.web3j.protocol.ipc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class IpcServiceTest {

    private static final String RESPONSE = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":\"Geth/v1.5.4-stable-b70acf3c/darwin/go1.7.3\"}\n";

    private PrintWriter writer;
    private InputStreamReader reader;

    private IpcService ipcService;

    @Before
    public void setUp() {
        writer = mock(PrintWriter.class);
        reader = mock(InputStreamReader.class);

    }

    @Test
    public void testIpcService() throws IOException {
        ipcService = new IpcService("test", reader, writer, RESPONSE.length());

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((CharBuffer) args[0]).append(RESPONSE);
            return RESPONSE.length(); // void method, so return null
        }).when(reader).read(any(CharBuffer.class));

        runTest();
    }

    @Test
    public void testReadExceedsBuffer() throws IOException {
        int bufferSize = RESPONSE.length() / 3;

        ipcService = new IpcService("test", reader, writer, RESPONSE.length() / 3);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((CharBuffer) args[0]).append(RESPONSE.substring(0, bufferSize));
            return RESPONSE.length();
        })
                .doAnswer(
                invocation -> {
                    Object[] args = invocation.getArguments();
                    ((CharBuffer) args[0]).append(
                            RESPONSE.substring(bufferSize, bufferSize * 2));
                    return RESPONSE.length();
                }).doAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    ((CharBuffer) args[0]).append(
                            RESPONSE.substring(bufferSize * 2, bufferSize * 3));
                    return RESPONSE.length();
                })
                .doAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    ((CharBuffer) args[0]).append(
                            RESPONSE.substring(bufferSize * 3, RESPONSE.length()));
                    return RESPONSE.length();
                })
                .when(reader).read(any(CharBuffer.class));

        runTest();
    }

    private void runTest() throws IOException {

        ipcService.send(new Request(), Web3ClientVersion.class);

        verify(writer).write("{\"jsonrpc\":\"2.0\",\"method\":null,\"params\":null,\"id\":0}");
        verify(writer).flush();
    }
}
