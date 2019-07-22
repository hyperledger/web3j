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
package org.web3j.protocol.parity.methods.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.protocol.core.DefaultBlockParameter;

/** TraceFilter used in trace_filter. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraceFilter {

    private DefaultBlockParameter fromBlock;
    private DefaultBlockParameter toBlock;
    private List<String> fromAddress;
    private List<String> toAddress;

    public TraceFilter(
            DefaultBlockParameter fromBlock,
            DefaultBlockParameter toBlock,
            List<String> fromAddress,
            List<String> toAddress) {
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getFromAddress() {
        return fromAddress;
    }

    public List<String> getToAddress() {
        return toAddress;
    }
}
