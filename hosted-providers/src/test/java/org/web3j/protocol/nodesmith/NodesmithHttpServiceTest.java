package org.web3j.protocol.nodesmith;

import java.util.Optional;

import okhttp3.Headers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodesmithHttpServiceTest {
    NodesmithHttpService service;

    @Test
    public void testParseHeaders() {
        Optional<RateLimitInfo> info = NodesmithHttpService.createRateLimitFromHeaders(Headers.of(
                NodesmithHttpService.NS_RATELIMIT_LIMIT, "500",
                NodesmithHttpService.NS_RATELIMIT_REMAINING, "442",
                NodesmithHttpService.NS_RATELIMIT_RESET, "1553385403"));

        assertTrue(info.isPresent());
        assertEquals(500, info.get().getTotalAllowedInWindow());
        assertEquals(442, info.get().getRemainingInWindow());
        assertEquals(1553385403L, info.get().getWindowResetTime().getEpochSecond());
    }

    @Test
    public void testParseHeader_FailureCases() {
        // Missing some of the required headers
        assertFalse(NodesmithHttpService.createRateLimitFromHeaders(
                Headers.of(NodesmithHttpService.NS_RATELIMIT_LIMIT, "42"))
                .isPresent());

        // Invalid number format
        assertFalse(NodesmithHttpService.createRateLimitFromHeaders(
                Headers.of(
                        NodesmithHttpService.NS_RATELIMIT_LIMIT, "42",
                        NodesmithHttpService.NS_RATELIMIT_REMAINING, "xyz",
                        NodesmithHttpService.NS_RATELIMIT_RESET, "1553385403"))
                .isPresent());

        // Null headers
        assertFalse(NodesmithHttpService.createRateLimitFromHeaders(null)
                .isPresent());
    }
}
