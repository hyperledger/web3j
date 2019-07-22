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
package org.web3j.tx.gas;

import java.math.BigInteger;

public class EeaGasProvider extends StaticGasProvider {
    public EeaGasProvider(BigInteger gasPrice) {
        super(gasPrice, BigInteger.valueOf(3000000));
    }

    public EeaGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        super(gasPrice, gasLimit);
    }
}
