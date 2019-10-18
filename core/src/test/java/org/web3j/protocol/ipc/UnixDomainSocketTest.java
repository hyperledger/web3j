/*
 * Copyright 2019 Web3 Labs Ltd.
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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnixDomainSocketTest {

    private static final String RESPONSE =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor "
                    + "incididunt ut labore et dolore magna aliqua\n";

    private PrintWriter writer;
    private InputStreamReader reader;

    private UnixDomainSocket unixDomainSocket;

    @Before
    public void setUp() {
        writer = mock(PrintWriter.class);
        reader = mock(InputStreamReader.class);
    }

    @Test
    public void testIpcService() throws IOException {
        unixDomainSocket = new UnixDomainSocket(reader, writer, RESPONSE.length());

        doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            ((CharBuffer) args[0]).append(RESPONSE);
                            return RESPONSE.length(); // void method, so return null
                        })
                .when(reader)
                .read(any(CharBuffer.class));

        runTest();
    }

    @Test
    public void testReadExceedsBuffer() throws IOException {
        int bufferSize = RESPONSE.length() / 3;

        unixDomainSocket = new UnixDomainSocket(reader, writer, RESPONSE.length() / 3);

        doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            ((CharBuffer) args[0]).append(RESPONSE.substring(0, bufferSize));
                            return RESPONSE.length();
                        })
                .doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            ((CharBuffer) args[0])
                                    .append(RESPONSE.substring(bufferSize, bufferSize * 2));
                            return RESPONSE.length();
                        })
                .doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            ((CharBuffer) args[0])
                                    .append(RESPONSE.substring(bufferSize * 2, bufferSize * 3));
                            return RESPONSE.length();
                        })
                .doAnswer(
                        invocation -> {
                            Object[] args = invocation.getArguments();
                            ((CharBuffer) args[0])
                                    .append(RESPONSE.substring(bufferSize * 3, RESPONSE.length()));
                            return RESPONSE.length();
                        })
                .when(reader)
                .read(any(CharBuffer.class));

        runTest();
    }

    private void runTest() throws IOException {

        unixDomainSocket.write("test request");

        verify(writer).write("test request");
        verify(writer).flush();
    }

    @Test
    public void testSlowResponse() throws Exception {
        String response =
                "{\"jsonrpc\":\"2.0\",\"id\":1,"
                        + "\"result\":\"Geth/v1.5.4-stable-b70acf3c/darwin/go1.7.3\"}\n";
        unixDomainSocket = new UnixDomainSocket(reader, writer, response.length());
        final LinkedList<String> segments = new LinkedList<>();
        // 1st part of response
        segments.add(response.substring(0, 50));
        // rest of response
        segments.add(response.substring(50));
        doAnswer(
                        invocation -> {
                            String segment = segments.poll();
                            if (segment == null) {
                                return 0;
                            } else {
                                Object[] args = invocation.getArguments();
                                ((CharBuffer) args[0]).append(segment);
                                return segment.length();
                            }
                        })
                .when(reader)
                .read(any(CharBuffer.class));

        IpcService ipcService =
                new IpcService() {
                    @Override
                    protected IOFacade getIO() {
                        return unixDomainSocket;
                    }
                };
        ipcService.send(new Request(), Web3ClientVersion.class);
    }
}
