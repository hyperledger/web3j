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
package org.web3j.codegen.generators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolidityConstants {

    public static final String BINARY = "BINARY";
    public static final String WEB3J = "web3j";
    public static final String CREDENTIALS = "credentials";
    public static final String CONTRACT_GAS_PROVIDER = "contractGasProvider";
    public static final String TRANSACTION_MANAGER = "transactionManager";
    public static final String INITIAL_VALUE = "initialWeiValue";
    public static final String CONTRACT_ADDRESS = "contractAddress";
    public static final String GAS_PRICE = "gasPrice";
    public static final String GAS_LIMIT = "gasLimit";
    public static final String FILTER = "filter";
    public static final String START_BLOCK = "startBlock";
    public static final String END_BLOCK = "endBlock";
    public static final String WEI_VALUE = "weiValue";
    public static final String FUNC_NAME_PREFIX = "FUNC_";
    public static final String TYPE_FUNCTION = "function";
    public static final String TYPE_EVENT = "event";

    public static final Logger LOGGER = LoggerFactory.getLogger(SolidityWrapperGenerator.class);
}
