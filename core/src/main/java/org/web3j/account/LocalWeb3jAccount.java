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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.web3j.protocol.Network;
import org.web3j.protocol.http.HttpService;

public class LocalWeb3jAccount {

    private static final Path web3jConfigPath =
            Paths.get(System.getProperty("user.home"), ".web3j", ".config");

    static String SERVICES_ENDPOINT = "http://localhost/api/rpc/%s/%s/";

    public static HttpService getOnlineServicesHttpService(final Network network) throws Exception {
        if (web3jConfigPath.toFile().exists()) {
            String configContents = new String(Files.readAllBytes(web3jConfigPath));
            final ObjectNode node = new ObjectMapper().readValue(configContents, ObjectNode.class);
            if (node.has("loginToken")) {
                String httpEndpoint =
                        String.format(
                                SERVICES_ENDPOINT,
                                network.getNetworkName(),
                                node.get("loginToken").asText());
                return new HttpService(httpEndpoint);
            }
        }
        throw new IllegalStateException(
                "Config file does not exist or could not be read. In order to use Web3j without a specified endpoint, you must use the CLI and log in to Web3j Cloud");
    }
}
