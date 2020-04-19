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

import java.io.*;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.web3j.protocol.Network;
import org.web3j.protocol.http.HttpService;

public class LocalWeb3jAccount {

    private static final File EPIRUS_CONFIG_FILE =
            new File(System.getProperty("user.home"), ".epirus.config");

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
        return EPIRUS_CONFIG_FILE.exists();
    }

    public static ObjectNode readConfigAsJson() throws IOException {
        File file = EPIRUS_CONFIG_FILE;
        InputStream in = new FileInputStream(file);
        byte[] b = new byte[(int) file.length()];
        int len = b.length;
        int total = 0;

        while (total < len) {
            int result = in.read(b, total, len - total);
            if (result == -1) {
                break;
            }
            total += result;
        }

        String configContents = new String(b, Charset.forName("UTF-8"));
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
