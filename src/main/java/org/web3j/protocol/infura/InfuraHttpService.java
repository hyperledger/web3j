package org.web3j.protocol.infura;

import java.util.Collections;
import java.util.Map;

import org.web3j.protocol.http.HttpService;

/**
 * HttpService for working with <a href="https://infura.io/">Infura</a> clients.
 */
public class InfuraHttpService extends HttpService {

    private static final String INFURA_ETHEREUM_PREFERRED_CLIENT =
            "Infura-Ethereum-Preferred-Client";

    private static final char[] TEMP_KEY_STORE_PASSWORD = "web3j runtime cert store".toCharArray();

    private final Map<String, String> clientVersionHeader;

    public InfuraHttpService(String url, String clientVersion, boolean required) {
        super(url);
        clientVersionHeader = buildClientVersionHeader(clientVersion, required);
    }

    public InfuraHttpService(String url, String clientVersion) {
        this(url, clientVersion, true);
    }

    public InfuraHttpService(String url) {
        this(url, "", false);
    }

    @Override
    protected void addHeaders(Map<String, String> headers) {
        headers.putAll(clientVersionHeader);
    }

    static Map<String, String> buildClientVersionHeader(String clientVersion, boolean required) {
        if (clientVersion == null || clientVersion.equals("")) {
            return Collections.emptyMap();
        }

        if (required) {
            return Collections.singletonMap(INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion);
        } else {
            return Collections.singletonMap(
                    INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion + "; required=false");
        }
    }
}
