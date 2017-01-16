package org.web3j.protocol.ipc;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.Channels;

import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/**
 * Unix socket implementation of our services API.
 *
 * This is experimental - it's only been tested on OS X currently.
 */
public class IpcService extends Service {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private static final Log log = LogFactory.getLog(IpcService.class);

    private final int bufferSize;

    private final InputStreamReader reader;
    private final PrintWriter writer;

    public IpcService(String ipcSocketPath) {
        this(ipcSocketPath, DEFAULT_BUFFER_SIZE);
    }

    public IpcService(String ipcSocketPath, int bufferSize) {
        this.bufferSize = bufferSize;

        try {
            UnixSocketAddress address = new UnixSocketAddress(ipcSocketPath);
            UnixSocketChannel channel = UnixSocketChannel.open(address);

            reader = new InputStreamReader(Channels.newInputStream(channel));
            writer = new PrintWriter(Channels.newOutputStream(channel));

        } catch (IOException e) {
            throw new RuntimeException(
                    "Provided file socket cannot be opened: " + ipcSocketPath, e);
        }
    }

    IpcService(String ipcSocketPath, InputStreamReader reader, PrintWriter writer, int bufferSize) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        String payload = objectMapper.writeValueAsString(request);

        writer.write(payload);
        writer.flush();
        log.debug(">> " + payload);

        CharBuffer response = CharBuffer.allocate(bufferSize);
        String result = "";

        do {
            response.clear();
            reader.read(response);
            result += new String(response.array(), response.arrayOffset(), response.position());
        } while (response.position() == response.limit()
                && response.get(response.limit() - 1) != '\n');

        log.debug("<< " + result);
        return objectMapper.readValue(result, responseType);
    }
}
