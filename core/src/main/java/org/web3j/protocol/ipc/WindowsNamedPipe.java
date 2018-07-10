package org.web3j.protocol.ipc;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Windows named pipe IO implementation for IPC.
 */
public class WindowsNamedPipe implements IOFacade {

    private final RandomAccessFile pipe;

    public WindowsNamedPipe(String ipcSocketPath) {
        try {
            pipe = new RandomAccessFile(ipcSocketPath, "rw");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Provided file pipe cannot be opened: " + ipcSocketPath, e);
        }
    }

    @Override
    public void write(String payload) throws IOException {
        pipe.write(payload.getBytes());
    }

    @Override
    public String read() throws IOException {
        return pipe.readLine();
    }

    @Override
    public void close() throws IOException {
        pipe.close();
    }
}
