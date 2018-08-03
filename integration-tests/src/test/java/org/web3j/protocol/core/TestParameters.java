package org.web3j.protocol.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.web3j.protocol.http.HttpService.DEFAULT_URL;

/**
 * Constants here can be configured via environment variable, or via the contents of
 * local-test.properties, or via the system property. The environment variable name or property
 * entry name or system property name used per constant is WEB3J_&lt;CONSTANT_NAME&gt;.
 *
 * <p>For example, you can configure {@link #TEST_ROPSTEN_URL} by supplying a value to the
 * environment variable WEB3J_TEST_ROPSTEN_URL (i.e. export
 * WEB3J_TEST_ROPSTEN_URL="https://ropsten.infura.io/yourtoken") or by adding an entry in
 * local-test.properties for WEB3J_TEST_ROPSTEN_URL (i.e.
 * WEB3J_TEST_ROPSTEN_URL=https://ropsten.infura.io/yourtoken) or by supplying a value to the
 * system property WEB3J_TEST_ROPSTEN_URL (i.e.
 * -DWEB3J_TEST_ROPSTEN_URL="https://ropsten.infura.io/yourtoken").
 *
 * <p>The priority of lookup starts from the System property to the local-test.properties to the
 * environment variables. That is, if the variable exists both in the System property and in the
 * local-test.properties, then the one in the System Property would be used. Or if the variable
 * exists both in the local-test.properties and in the environment variable, then the entry in
 * the local-test.properties would be used.
 */
public class TestParameters {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestParameters.class);

    private static final String LOCAL_TEST_PROPERTIES_FILENAME = "local-test.properties";

    public static final String ALICE_PRIVKEY;

    public static final String ALICE_PUBKEY;

    public static final boolean BENCHMARK_TESTING_ON;

    public static final String BOB_PRIVKEY;

    public static final String BOB_PUBKEY;

    public static final int FAST_RAW_TRANSACTION_MANAGER_IT_COUNT;

    /**
     * For parity.
     */
    public static final String TEST_KOVAN_URL;

    /**
     * For geth.
     */
    public static final String TEST_RINKEBY_URL;

    /**
     * For both geth and parity.
     */
    public static final String TEST_ROPSTEN_URL;

    static {
        Properties localTestProperties = new Properties();
        File localTestPropertiesFile = new File(LOCAL_TEST_PROPERTIES_FILENAME);
        try (Reader reader = new FileReader(localTestPropertiesFile)) {
            localTestProperties.load(reader);
        } catch (IOException e) {
            LOGGER.warn("Could NOT read {}", localTestPropertiesFile.getAbsolutePath(), e);
            if (!localTestPropertiesFile.exists()) {
                LOGGER.info("Tip : You can copy .local-test.properties.example over to {} and set "
                        + "the test parameters there", localTestPropertiesFile.getAbsolutePath());
            }
        }

        ALICE_PRIVKEY = getPropertyOrEnv(localTestProperties, "WEB3J_ALICE_PRIVKEY", "");
        ALICE_PUBKEY = getPropertyOrEnv(localTestProperties, "WEB3J_ALICE_PUBKEY", "0x");
        BENCHMARK_TESTING_ON = getPropertyOrEnv(localTestProperties, "WEB3J_BENCHMARK_TESTING_ON",
            true);
        BOB_PRIVKEY = getPropertyOrEnv(localTestProperties, "WEB3J_BOB_PRIVKEY", "");
        BOB_PUBKEY = getPropertyOrEnv(localTestProperties, "WEB3J_BOB_PUBKEY", "0x");
        FAST_RAW_TRANSACTION_MANAGER_IT_COUNT = getPropertyOrEnv(localTestProperties,
            "WEB3J_FAST_RAW_TRANSACTION_MANAGER_IT_COUNT", 1);

        TEST_ROPSTEN_URL = getPropertyOrEnv(localTestProperties, "WEB3J_TEST_ROPSTEN_URL",
            DEFAULT_URL);
        TEST_KOVAN_URL = getPropertyOrEnv(localTestProperties, "WEB3J_TEST_KOVAN_URL",
            TEST_ROPSTEN_URL);
        TEST_RINKEBY_URL = getPropertyOrEnv(localTestProperties, "WEB3J_TEST_RINKEBY_URL",
            TEST_ROPSTEN_URL);

        LOGGER.info(
                "\n"
                + " * WEB3J_ALICE_PRIVKEY = {}\n"
                + " * WEB3J_ALICE_PUBKEY = {}\n"
                + " * WEB3J_BENCHMARK_TESTING_ON = {}\n"
                + " * WEB3J_BOB_PRIVKEY = {}\n"
                + " * WEB3J_BOB_PUBKEY = {}\n"
                + " * WEB3J_FAST_RAW_TRANSACTION_MANAGER_IT_COUNT = {}\n"
                + " * WEB3J_TEST_KOVAN_URL = {}\n"
                + " * WEB3J_TEST_RINKEBY_URL = {}\n"
                + " * WEB3J_TEST_ROPSTEN_URL = {}\n"
                + "{}",
                ALICE_PRIVKEY,
                ALICE_PUBKEY,
                BENCHMARK_TESTING_ON,
                BOB_PRIVKEY,
                BOB_PUBKEY,
                FAST_RAW_TRANSACTION_MANAGER_IT_COUNT,
                TEST_KOVAN_URL,
                TEST_RINKEBY_URL,
                TEST_ROPSTEN_URL,
                "");
    }

    private static Boolean getPropertyOrEnv(
            Properties localTestProperties,
            String name,
            Boolean defaultValue) {
        return getPropertyOrEnv(localTestProperties, name, Boolean::valueOf, defaultValue);
    }

    private static Integer getPropertyOrEnv(
            Properties localTestProperties,
            String name,
            Integer defaultValue) {
        return getPropertyOrEnv(localTestProperties, name, Integer::valueOf, defaultValue);
    }

    private static String getPropertyOrEnv(
            Properties localTestProperties,
            String name,
            String defaultValue) {
        return getPropertyOrEnv(localTestProperties, name, String::valueOf, defaultValue);
    }



    private static <T> T getPropertyOrEnv(
            Properties localTestProperties,
            String name,
            Function<String, T> valueConverter,
            T defaultValue) {
        String propertyValue = System.getProperty(name,
                localTestProperties.getProperty(name,
                System.getenv(name)));

        return propertyValue != null ? valueConverter.apply(propertyValue) : defaultValue;
    }

    public static boolean isInfuraTestKovanUrl() {
        return isInfuraTestUrl(TestParameters.TEST_KOVAN_URL);
    }

    public static boolean isInfuraTestRinkebyUrl() {
        return isInfuraTestUrl(TestParameters.TEST_RINKEBY_URL);
    }

    private static boolean isInfuraTestUrl(String url) {
        return url.contains("infura.io");
    }
}
