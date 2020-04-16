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
package org.web3j.protocol.nodesmith;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.OkHttpClient;

import org.web3j.protocol.http.HttpService;

/**
 * HttpService for working with <a href="https://nodesmith.io/">Nodesmith's</a> hosted
 * infrastructure.
 */
public class NodesmithHttpService extends HttpService {

    static final String NS_RATELIMIT_LIMIT = "x-ratelimit-limit";
    static final String NS_RATELIMIT_REMAINING = "x-ratelimit-remaining";
    static final String NS_RATELIMIT_RESET = "x-ratelimit-reset";

    private RateLimitInfo latestRateLimitInfo = null;

    public NodesmithHttpService(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    public NodesmithHttpService(String url) {
        super(url);
    }

    public RateLimitInfo getLatestRateLimitInfo() {
        return this.latestRateLimitInfo;
    }

    @Override
    protected void processHeaders(Headers headers) {
        Optional<RateLimitInfo> info = createRateLimitFromHeaders(headers);
        if (info.isPresent()) {
            this.latestRateLimitInfo = info.get();
        }
    }

    static Optional<RateLimitInfo> createRateLimitFromHeaders(Headers headers) {
        if (headers == null) {
            return Optional.empty();
        }

        Set<String> names = headers.names();
        if (!names.containsAll(
                Arrays.asList(NS_RATELIMIT_LIMIT, NS_RATELIMIT_REMAINING, NS_RATELIMIT_RESET))) {
            return Optional.empty();
        }

        return RateLimitInfo.createFromHeaders(
                headers.get(NS_RATELIMIT_LIMIT),
                headers.get(NS_RATELIMIT_REMAINING),
                headers.get(NS_RATELIMIT_RESET));
    }
}
