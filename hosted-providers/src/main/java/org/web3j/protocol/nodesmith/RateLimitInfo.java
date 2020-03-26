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

import java.time.Instant;
import java.util.Optional;

/**
 * Represents the current status of an api key's rate limit for Nodesmith's service. This class can
 * be used to inspect when more requests can be sent.
 *
 * @see <a href="https://beta.docs.nodesmith.io/#/ethereum/rateLimiting">Rate Limiting</a>
 */
public class RateLimitInfo {

    private final long limit;
    private final long remaining;
    private final Instant resetTime;

    RateLimitInfo(final long limit, final long remaining, final Instant resetTime) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetTime = resetTime;
    }

    /**
     * The total rate limit for this API key and endpoint.
     *
     * @return total rate limit allowed.
     */
    public long getTotalAllowedInWindow() {
        return this.limit;
    }

    /**
     * How many more requests can be sent in to current window.
     *
     * @return Number of requests remaining.
     */
    public long getRemainingInWindow() {
        return this.remaining;
    }

    /**
     * When the current window ends and the rate limit will be reset.
     *
     * @return Time when the rate limit resets.
     */
    public Instant getWindowResetTime() {
        return this.resetTime;
    }

    /**
     * Tries to create a new instance of the RateLimitInfo class from the headers passed in. If
     * successful, it returns the instance wrapped in Optional, otherwise Optional.empty().
     *
     * @param limitValue The x-ratelimit-limit header value.
     * @param remainingValue The x-ratelimit-remaining header value.
     * @param resetTimeValue The x-ratelimit-reset header value.
     * @return If successful, RateLimitInfo wrapped in Optional, otherwise Optional.empty().
     */
    public static Optional<RateLimitInfo> createFromHeaders(
            final String limitValue, final String remainingValue, final String resetTimeValue) {

        try {
            final long limit = Long.parseLong(limitValue);
            final long remaining = Long.parseLong(remainingValue);
            final long resetEpochSeconds = Long.parseLong(resetTimeValue);
            final Instant resetTime = Instant.ofEpochSecond(resetEpochSeconds);
            return Optional.of(new RateLimitInfo(limit, remaining, resetTime));
        } catch (final NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
