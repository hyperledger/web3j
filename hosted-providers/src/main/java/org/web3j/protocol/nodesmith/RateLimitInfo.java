package org.web3j.protocol.nodesmith;

import java.time.Instant;
import java.util.Optional;

/**
 * Represents the current status of an api key's
 * <a href="https://beta.docs.nodesmith.io/#/ethereum/rateLimiting>rate limit</a> for Nodesmith's
 * service. This class can be used to inspect when more requests can be sent.
 */
public class RateLimitInfo {

    private long limit;
    private long remaining;
    private Instant resetTime;

    RateLimitInfo(long limit, long remaining, Instant resetTime) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetTime = resetTime;
    }

    /**
     * The total rate limit for this API key and endpoint.
     * @return total rate limit allowed.
     */
    public long getTotalAllowedInWindow() {
        return this.limit;
    }

    /**
     * How many more requests can be sent in to current window.
     * @return Number of requests remaining.
     */
    public long getRemainingInWindow() {
        return this.remaining;
    }

    /**
     * When the current window ends and the rate limit will be reset.
     * @return Time when the rate limit resets.
     */
    public Instant getWindowResetTime() {
        return this.resetTime;
    }

    /**
     * Tries to create a new instance of the RateLimitInfo class from the headers passed in.
     * If successful, it returns the instance wrapped in Optional, otherwise Optional.empty().
     * @param limitValue The x-ratelimit-limit header value.
     * @param remainingValue The x-ratelimit-remaining header value.
     * @param resetTimeValue The x-ratelimit-reset header value.
     * @return If successful, RateLimitInfo wrapped in Optional, otherwise Optional.empty().
     */
    public static Optional<RateLimitInfo> createFromHeaders(
            String limitValue, String remainingValue, String resetTimeValue) {

        try {
            long limit = Long.parseLong(limitValue);
            long remaining = Long.parseLong(remainingValue);
            long resetEpochSeconds = Long.parseLong(resetTimeValue);
            Instant resetTime = Instant.ofEpochSecond(resetEpochSeconds);
            return Optional.of(new RateLimitInfo(limit, remaining, resetTime));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
