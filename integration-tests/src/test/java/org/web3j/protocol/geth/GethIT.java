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
package org.web3j.protocol.geth;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.TxPoolContent;
import org.web3j.protocol.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@EVMTest(type = NodeType.BESU)
public class GethIT {

    private Geth web3j;

    public GethIT() {}

    @BeforeEach
    public void setUp() {
        this.web3j = Geth.build(new HttpService());
    }

    @Disabled
    @Test // Method not Implemented.
    public void testWeb3ClientVersion() throws Exception {
        TxPoolContent content = web3j.txPoolContent().send();
        assertNotNull(content.getResult());
    }

    @Test
    public void testPersonalAccountCreation() throws IOException {
        Admin admin = Admin.build(new HttpService());
        NewAccountIdentifier accountId = admin.personalNewAccount("web3j-geth-IT").send();
        assertFalse(accountId.getResult().isEmpty());
    }
}
