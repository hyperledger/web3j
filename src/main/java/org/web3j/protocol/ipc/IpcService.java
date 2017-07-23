package org.web3j.protocol.ipc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/**
 * Ipc service implementation.
 */
public class IpcService extends Service {

    private static final Logger log = LoggerFactory.getLogger(IpcService.class);

    private final IOFacade ioFacade;

    public IpcService(IOFacade ioFacade, boolean includeRawResponses) {
        super(includeRawResponses);
        this.ioFacade = ioFacade;
    }

    public IpcService(IOFacade ioFacade) {
        this(ioFacade, false);
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
