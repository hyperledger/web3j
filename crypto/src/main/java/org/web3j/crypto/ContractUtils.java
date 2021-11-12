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
package org.web3j.crypto;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.web3j.crypto.Keys.ADDRESS_SIZE;

/** Smart Contract utility functions. */
public class ContractUtils {

    public static final int SALT_SIZE = 32;
    public static final int ADDRESS_BYTE_SIZE = ADDRESS_SIZE / 8;

    /**
     * Generate a smart contract address. This enables you to identify what address a smart contract
     * will be deployed to on the network.
     *
     * @param address of sender
     * @param nonce of transaction
     * @return the generated smart contract address
     */
    public static byte[] generateContractAddress(byte[] address, BigInteger nonce) {
        List<RlpType> values = new ArrayList<>();

        values.add(RlpString.create(address));
        values.add(RlpString.create(nonce));
        RlpList rlpList = new RlpList(values);

        byte[] encoded = RlpEncoder.encode(rlpList);
        byte[] hashed = Hash.sha3(encoded);
        return Arrays.copyOfRange(hashed, 12, hashed.length);
    }

    public static String generateContractAddress(String address, BigInteger nonce) {
        byte[] result = generateContractAddress(Numeric.hexStringToByteArray(address), nonce);
        return Numeric.toHexString(result);
    }

    /**
     * Generate a CREATE2 smart contract address. <a
     * href="https://eips.ethereum.org/EIPS/eip-1014</a>.
     *
     * @param address  The address which is creating this new address
     * @param salt     A 32 bytes salt
     * @param initCode The init code of the contract being created
     * @return the generated CREATE2 smart contract address
     */
    public static byte[] generateCreate2ContractAddress(byte[] address, byte[] salt, byte[] initCode) {
        if (address.length != ADDRESS_BYTE_SIZE) {
            throw new RuntimeException("Invalid address size");
        }
        if (salt.length != SALT_SIZE) {
            throw new RuntimeException("Invalid salt size");
        }
        byte[] hashedInitCode = Hash.sha3(initCode);
        byte[] buffer = new byte[1 + address.length + salt.length + hashedInitCode.length];

        buffer[0] = (byte) 0xff;
        int offset = 1;
        System.arraycopy(address, 0, buffer, offset, address.length);
        offset += address.length;
        System.arraycopy(salt, 0, buffer, offset, salt.length);
        offset += salt.length;
        System.arraycopy(hashedInitCode, 0, buffer, offset, hashedInitCode.length);

        byte[] hashed = Hash.sha3(buffer);
        return Arrays.copyOfRange(hashed, 12, hashed.length);
    }

    public static String generateCreate2ContractAddress(String address, byte[] salt, byte[] initCode) {
        byte[] result = generateCreate2ContractAddress(Numeric.hexStringToByteArray(address), salt, initCode);
        return Numeric.toHexString(result);
    }
}
