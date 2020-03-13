/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.account;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.web3j.protocol.Network;
import org.web3j.protocol.http.HttpService;

public class LocalWeb3jAccount {

    private static final Path EPIRUS_CONFIG_PATh =
            Paths.get(System.getProperty("user.home"), ".epirus", ".config");

    private static String NODE_RPC_ENDPOINT = "https://%s-eth.epirus.io/%s";

    public static HttpService getOnlineServicesHttpService(final Network network) throws Exception {
        if (configExists()) {

            final ObjectNode node = readConfigAsJson();
            if (loginTokenExists(node)) {
                return createHttpServiceWithToken(network);
            }
        }
        throw new IllegalStateException(
                "Config file does not exist or could not be read. In order to use Web3j without a specified endpoint, you must use the CLI and log in to Web3j Cloud");
    }

    public static boolean configExists() {
        return EPIRUS_CONFIG_PATh.toFile().exists();
    }

    public static ObjectNode readConfigAsJson() throws IOException {
        String configContents = new String(Files.readAllBytes(EPIRUS_CONFIG_PATh));
        return new ObjectMapper().readValue(configContents, ObjectNode.class);
    }

    public static boolean loginTokenExists(ObjectNode node) {
        return node.has("loginToken");
    }

    public static HttpService createHttpServiceWithToken(Network network) throws IOException {
        String httpEndpoint =
                String.format(
                        NODE_RPC_ENDPOINT,
                        network.getNetworkName(),
                        readConfigAsJson().get("loginToken").asText());
        return new HttpService(httpEndpoint);
    }
}
