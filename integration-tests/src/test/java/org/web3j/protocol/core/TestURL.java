package org.web3j.protocol.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.web3j.protocol.http.HttpService.DEFAULT_URL;

public class TestURL {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestURL.class);

    /**
     * This can be configured via environment variable WEB3J_TEST_URL (i.e. export
     * WEB3J_TEST_URL=...) or via System Property (-DWEB3J_TEST_URL=...). If both are configured,
     * the value of the System property will be used over the environment variable. If neither is
     * set, this defaults to {@link org.web3j.protocol.http.HttpService#DEFAULT_URL}
     */
    public static final String URL;

    static {
        String url = System.getProperty("WEB3J_TEST_URL", System.getenv("WEB3J_TEST_URL"));
        if (url == null) {
            url = DEFAULT_URL;
        }
        URL = url;

        LOGGER.info("URL = {}", URL);
    }

    public static boolean isInfura() {
        return TestURL.URL.contains("infura.io");
    }
}
