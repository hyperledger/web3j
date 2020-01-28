package org.web3j.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.web3j.protocol.Network;
import org.web3j.protocol.http.HttpService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalWeb3jAccount {

    private static final Path web3jConfigPath =
            Paths.get(System.getProperty("user.home"), ".web3j", ".config");

    static String SERVICES_ENDPOINT = "http://localhost/api/geth/%s/%s/";

    public static HttpService getOnlineServicesHttpService(final Network network) throws Exception {
        if (web3jConfigPath.toFile().exists()) {
            String configContents = new String(Files.readAllBytes(web3jConfigPath));
            final ObjectNode node = new ObjectMapper().readValue(configContents, ObjectNode.class);
            if (node.has("loginToken")) {
                String httpEndpoint = String.format(SERVICES_ENDPOINT, network.getNetworkName(), node.get("loginToken").asText());
                return new HttpService(httpEndpoint);
            }
        }
        throw new IllegalStateException("Config file does not exist or could not be read. In order to use Web3j without a specified endpoint, you must use the CLI and log in to Web3j Cloud");
    }

}
