package org.web3j.protocol.ipc;

/**
 * Unix domain socket implementation of our services API.
 */
public class UnixIpcService extends IpcService {

    public UnixIpcService(String ipcSocketPath) {
        super(new UnixDomainSocket(ipcSocketPath));
    }

    public UnixIpcService(String ipcSocketPath, boolean includeRawResponse) {
        super(new UnixDomainSocket(ipcSocketPath), includeRawResponse);
    }
}
