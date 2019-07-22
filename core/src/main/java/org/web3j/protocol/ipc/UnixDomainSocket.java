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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;

import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

/** Unix domain socket IO implementation for IPC. */
public class UnixDomainSocket implements IOFacade {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final int bufferSize;

    private final InputStreamReader reader;
    private final PrintWriter writer;

    private final UnixSocketChannel channel;

    public UnixDomainSocket(String ipcSocketPath) {
        this(ipcSocketPath, DEFAULT_BUFFER_SIZE);
    }

    public UnixDomainSocket(String ipcSocketPath, int bufferSize) {
        this.bufferSize = bufferSize;

        try {
            UnixSocketAddress address = new UnixSocketAddress(ipcSocketPath);
            channel = UnixSocketChannel.open(address);

            reader = new InputStreamReader(Channels.newInputStream(channel));
            writer = new PrintWriter(Channels.newOutputStream(channel));

        } catch (IOException e) {
            throw new RuntimeException(
                    "Provided file socket cannot be opened: " + ipcSocketPath, e);
        }
    }

    UnixDomainSocket(InputStreamReader reader, PrintWriter writer, int bufferSize) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.reader = reader;
        this.channel = null;
    }

    @Override
    public void write(String payload) throws IOException {
        writer.write(payload);
        writer.flush();
    }

    @Override
    public String read() throws IOException {
        CharBuffer response = CharBuffer.allocate(bufferSize);
        StringBuilder result = new StringBuilder();

        do {
            response.clear();
            reader.read(response);
            result.append(response.array(), response.arrayOffset(), response.position());
        } while (response.get(response.position() - 1) != '\n');

        return result.toString();
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        if (null != channel) {
            channel.close();
        }
    }
}
