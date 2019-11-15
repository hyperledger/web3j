/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.ens;

import org.junit.jupiter.api.Test;

import org.web3j.tx.ChainId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.web3j.ens.Contracts.MAINNET;
import static org.web3j.ens.Contracts.RINKEBY;
import static org.web3j.ens.Contracts.ROPSTEN;
import static org.web3j.ens.Contracts.resolveRegistryContract;

@SuppressWarnings("deprecation")
public class ContractsTest {

    @Test
    public void testResolveRegistryContract() {
        assertEquals(resolveRegistryContract(ChainId.MAINNET + ""), (MAINNET));
        assertEquals(resolveRegistryContract(ChainId.ROPSTEN + ""), (ROPSTEN));
        assertEquals(resolveRegistryContract(ChainId.RINKEBY + ""), (RINKEBY));
    }

    @Test
    public void testResolveRegistryContractInvalid() {
        assertThrows(
                EnsResolutionException.class, () -> resolveRegistryContract(ChainId.NONE + ""));
    }
}
