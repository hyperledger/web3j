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

import org.junit.jupiter.api.Test;

import org.web3j.protocol.Network;
import org.web3j.protocol.http.HttpService;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpirusAccountTest {

    @Test
    public void testHttpServiceEnvironmentVariable() throws Exception {
        withEnvironmentVariable("EPIRUS_LOGIN_TOKEN", "token")
                .and("EPIRUS_APP_URL", "http://localhost:8000")
                .execute(
                        () -> {
                            HttpService service =
                                    LocalEpirusAccount.getEpirusHttpService(Network.RINKEBY);
                            assertEquals(
                                    "http://localhost:8000/api/rpc/rinkeby/token/",
                                    service.getUrl());
                        });
    }
}
