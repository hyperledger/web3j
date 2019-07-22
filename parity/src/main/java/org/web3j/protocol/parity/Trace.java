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
package org.web3j.protocol.parity;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.parity.methods.request.TraceFilter;
import org.web3j.protocol.parity.methods.response.ParityFullTraceResponse;
import org.web3j.protocol.parity.methods.response.ParityTraceGet;
import org.web3j.protocol.parity.methods.response.ParityTracesResponse;

/** * JSON-RPC Parity traces API request object building factory. */
public interface Trace {
    Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction, List<String> traceTypes, DefaultBlockParameter blockParameter);

    Request<?, ParityFullTraceResponse> traceRawTransaction(String data, List<String> traceTypes);

    Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes);

    Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter);

    Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter);

    Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices);

    Request<?, ParityTracesResponse> traceTransaction(String hash);
}
