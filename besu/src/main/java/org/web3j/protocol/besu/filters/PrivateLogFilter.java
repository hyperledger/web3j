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
package org.web3j.protocol.besu.filters;

import java.io.IOException;
import java.math.BigInteger;
import java8.util.Optional;

import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.filters.Callback;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

public class PrivateLogFilter extends LogFilter {

    private final String privacyGroupId;

    public PrivateLogFilter(
            Besu web3j,
            Callback<Log> callback,
            String privacyGroupId,
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        super(web3j, callback, ethFilter);
        this.privacyGroupId = privacyGroupId;
    }

    @Override
    protected EthFilter sendRequest() throws IOException {
        return ((Besu) web3j).privNewFilter(privacyGroupId, ethFilter).send();
    }

    @Override
    protected EthUninstallFilter uninstallFilter(BigInteger filterId) throws IOException {
        return ((Besu) web3j)
                .privUninstallFilter(privacyGroupId, Numeric.toHexStringWithPrefix(filterId))
                .send();
    }

    @Override
    protected Optional<Request<?, EthLog>> getFilterLogs(BigInteger filterId) {
        return Optional.of(
                ((Besu) web3j)
                        .privGetFilterLogs(
                                privacyGroupId, Numeric.toHexStringWithPrefix(filterId)));
    }
}
