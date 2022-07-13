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

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/** Keys generated for unit testing purposes. */
public class SampleR1Keys {

    public static final String PRIVATE_KEY_STRING =
            "841751f2469044902cd0190724aff81233447d5da96dd4b86eb4e632a5c7f32d";
    static final String PUBLIC_KEY_STRING =
            "0x59320b8c6a805b6feb8295b8663bbe696f30d65115138b8a35c320215f3f0ba3e4525befe27caaee7818b7ee66485503031d74a6150b70b079dfbd7171eaf57";
    public static final String ADDRESS = "0x7eb4e4c200d9f2bd45dc1701680974948ac07717";

    static final BigInteger PRIVATE_KEY = Numeric.toBigInt(PRIVATE_KEY_STRING);
    static final BigInteger PUBLIC_KEY = Numeric.toBigInt(PUBLIC_KEY_STRING);

    static final ECKeyPair KEY_PAIR =
            new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY, Sign.NIST_CURVE.getCurve());

    private SampleR1Keys() {}
}
