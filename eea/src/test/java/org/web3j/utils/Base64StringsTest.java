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
package org.web3j.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base64StringsTest {

    private static final String BASE64_1 = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
    private static final List<String> BASE64_LIST = Arrays.asList(BASE64_1, BASE64_1);

    private static final Base64String BASE64_WRAPPED =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final List<Base64String> BASE64_WRAPPED_LIST =
            Arrays.asList(BASE64_WRAPPED, BASE64_WRAPPED);

    @Test
    public void testWrapList() {
        assertEquals(BASE64_WRAPPED_LIST, Base64Strings.wrapList(BASE64_LIST));
    }

    @Test
    public void testUnwrapList() {
        assertEquals(BASE64_LIST, Base64Strings.unwrapList(BASE64_WRAPPED_LIST));
    }
}
