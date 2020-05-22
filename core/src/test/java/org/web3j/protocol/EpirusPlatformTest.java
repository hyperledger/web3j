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
package org.web3j.protocol;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpirusPlatformTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @Test
    public void testWeb3jCloudIsFunctionalAgainstMock() throws Exception {
        stubFor(
                post(urlPathMatching("/api/rpc/mainnet/token/"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                "{\n"
                                                        + "  \"id\":67,\n"
                                                        + "  \"jsonrpc\":\"2.0\",\n"
                                                        + "  \"result\": \"Mist/v0.9.3/darwin/go1.4.1\"\n"
                                                        + "}")));

        withEnvironmentVariable("EPIRUS_LOGIN_TOKEN", "token")
                .and("EPIRUS_APP_URL", wireMockServer.baseUrl())
                .execute(
                        () -> {
                            Web3j web3j = Web3j.build();

                            String netVersion =
                                    web3j.web3ClientVersion().send().getWeb3ClientVersion();
                            assertEquals("Mist/v0.9.3/darwin/go1.4.1", netVersion);
                        });
    }
}
