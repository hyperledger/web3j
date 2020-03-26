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
package org.web3j.utils;

import java.io.IOException;
import java.util.Properties;

/** Build version utility method. */
public class Version {

    private Version() {}

    public static final String DEFAULT = "none";

    private static final String TIMESTAMP = "timestamp";
    private static final String VERSION = "version";

    public static String getVersion() throws IOException {
        return loadProperties().getProperty(VERSION);
    }

    public static String getTimestamp() throws IOException {
        return loadProperties().getProperty(TIMESTAMP);
    }

    private static Properties loadProperties() throws IOException {
        final Properties properties = new Properties();
        properties.load(Version.class.getResourceAsStream("/web3j-version.properties"));
        return properties;
    }
}
