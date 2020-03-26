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

import java.util.Optional;

import okhttp3.Headers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodesmithHttpServiceTest {
    NodesmithHttpService service;

    @Test
    public void testParseHeaders() {
        final Optional<RateLimitInfo> info =
                NodesmithHttpService.createRateLimitFromHeaders(
                        Headers.of(
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
        assertFalse(
                NodesmithHttpService.createRateLimitFromHeaders(
                                Headers.of(NodesmithHttpService.NS_RATELIMIT_LIMIT, "42"))
                        .isPresent());

        // Invalid number format
        assertFalse(
                NodesmithHttpService.createRateLimitFromHeaders(
                                Headers.of(
                                        NodesmithHttpService.NS_RATELIMIT_LIMIT, "42",
                                        NodesmithHttpService.NS_RATELIMIT_REMAINING, "xyz",
                                        NodesmithHttpService.NS_RATELIMIT_RESET, "1553385403"))
                        .isPresent());

        // Null headers
        assertFalse(NodesmithHttpService.createRateLimitFromHeaders(null).isPresent());
    }
}
