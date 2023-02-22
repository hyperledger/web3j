/*
 * Copyright 2023 Web3 Labs Ltd.
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
package org.web3j.abi.datatypes;

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.generated.StaticArray4;
import org.web3j.abi.datatypes.generated.Uint256;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticStructTest {
    @Test
    public void testStaticStruct() {
        Address address1 = Address.DEFAULT;
        Address address2 = Address.DEFAULT;
        StaticArray4<Uint256> array4 =
                new StaticArray4<>(
                        Uint256.class,
                        Uint256.DEFAULT,
                        Uint256.DEFAULT,
                        Uint256.DEFAULT,
                        Uint256.DEFAULT);
        StaticStruct struct = new StaticStruct(address1, address2, array4);

        // (address,address,uint256[4])
        String expected =
                "("
                        + address1.getTypeAsString()
                        + ","
                        + address2.getTypeAsString()
                        + ","
                        + array4.getTypeAsString()
                        + ")";
        assertEquals(expected, struct.getTypeAsString());
    }
}
