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

public class LocalEpirusAccount {

    private static final Path EPIRUS_CONFIG_PATH =
            Paths.get(System.getProperty("user.home"), ".epirus", ".config");

    public static HttpService getEpirusHttpService(final Network network) throws Exception {
        String envLoginToken =
                System.getenv().getOrDefault("EPIRUS_LOGIN_TOKEN", getConfigFileLoginToken());
        return createHttpServiceWithToken(network, envLoginToken);
    }

    private static String getConfigFileLoginToken() throws IOException {
        if (!EPIRUS_CONFIG_PATH.toFile().exists()) {
            throw new IllegalStateException(
                    "Config file does not exist. In order to use Web3j without a specified endpoint, you must use the Epirus CLI and log in to the Epirus Platform.");
        }
        String configContents = new String(Files.readAllBytes(EPIRUS_CONFIG_PATH));
        final ObjectNode node = new ObjectMapper().readValue(configContents, ObjectNode.class);
        if (node.has("loginToken")) {
            return node.get("loginToken").asText();
        }
        throw new IllegalStateException(
                "Epirus config file exists but does not contain a login token. Please log in to the Epirus platform using the CLI.");
    }

    private static HttpService createHttpServiceWithToken(Network network, String token) {
        String epirusBaseUrl =
                System.getenv().getOrDefault("EPIRUS_APP_URL", "https://app.epirus.io");
        String httpEndpoint =
                String.format("%s/api/rpc/%s/%s/", epirusBaseUrl, network.getNetworkName(), token);
        return new HttpService(httpEndpoint);
    }
}
