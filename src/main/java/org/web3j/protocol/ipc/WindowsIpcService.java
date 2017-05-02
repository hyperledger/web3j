package org.web3j.protocol.ipc;

/**
 * <p>Windows named pipe implementation of our services API.
 *
 * <p>This implementation is experimental.
 */
public class WindowsIpcService extends IpcService {

    public WindowsIpcService(String ipcSocketPath) {
        super(new WindowsNamedPipe(ipcSocketPath));
    }
}
