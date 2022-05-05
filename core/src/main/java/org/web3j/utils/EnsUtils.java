/*
 * Copyright 2022 Web3 Labs Ltd.
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
package org.web3j.utils;

public class EnsUtils {

    public static final String EMPTY_ADDRESS = "0x0000000000000000000000000000000000000000";

    // Wildcard resolution
    public static final byte[] ENSIP_10_INTERFACE_ID = Numeric.hexStringToByteArray("0x9061b923");
    public static final String EIP_3668_CCIP_INTERFACE_ID = "0x556f1830";

    public static boolean isAddressEmpty(String address) {
        return EMPTY_ADDRESS.equals(address);
    }

    public static boolean isEIP3668(String data) {
        if (data == null || data.length() < 10) {
            return false;
        }

        return EnsUtils.EIP_3668_CCIP_INTERFACE_ID.equals(data.substring(0, 10));
    }

    public static String getParent(String url) {
        String ensUrl = url != null ? url.trim() : "";

        if (ensUrl.equals(".") || !ensUrl.contains(".")) {
            return null;
        }

        return ensUrl.substring(ensUrl.indexOf(".") + 1);
    }
}
