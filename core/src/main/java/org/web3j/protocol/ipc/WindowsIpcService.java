package org.web3j.protocol.ipc;

/**
 * Windows named pipe implementation of our services API.
 *
 * <p>This implementation is experimental.
 */
public class WindowsIpcService extends IpcService {
    private final String ipcSocketPath;

    public WindowsIpcService(String ipcSocketPath) {
        super();
        this.ipcSocketPath = ipcSocketPath;
    }

    public WindowsIpcService(String ipcSocketPath, boolean includeRawResponse) {
        super(includeRawResponse);
        this.ipcSocketPath = ipcSocketPath;
    }

    @Override
    protected IOFacade getIO() {
        return new WindowsNamedPipe(ipcSocketPath);
    }
}
