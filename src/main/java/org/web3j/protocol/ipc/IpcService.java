package org.web3j.protocol.ipc;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/**
 * Ipc service implementation.
 */
public class IpcService extends Service {

    private static final Log log = LogFactory.getLog(IpcService.class);

    private final IOFacade ioFacade;

    public IpcService(IOFacade ioFacade) {
        super();
        this.ioFacade = ioFacade;
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        String payload = objectMapper.writeValueAsString(request);

        ioFacade.write(payload);
        log.debug(">> " + payload);

        String result = ioFacade.read();
        log.debug("<< " + result);

        return objectMapper.readValue(result, responseType);
    }
}
