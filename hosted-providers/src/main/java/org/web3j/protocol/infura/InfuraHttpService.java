/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.protocol.infura;

import java.util.Collections;
import java.util.Map;

import org.web3j.protocol.http.HttpService;

/** HttpService for working with <a href="https://infura.io/">Infura</a> clients. */
public class InfuraHttpService extends HttpService {

    private static final String INFURA_ETHEREUM_PREFERRED_CLIENT =
            "Infura-Ethereum-Preferred-Client";

    private final Map<String, String> clientVersionHeader;

    public InfuraHttpService(String url, String clientVersion, boolean required) {
        super(url);
        clientVersionHeader = buildClientVersionHeader(clientVersion, required);
        addHeaders(clientVersionHeader);
    }

    public InfuraHttpService(String url, String clientVersion) {
        this(url, clientVersion, true);
    }

    public InfuraHttpService(String url) {
        this(url, "", false);
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
