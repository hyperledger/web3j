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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Base64StringTest {

    private static final String BASE64_1 = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
    private static final byte[] BASE64_BYTES_1 =
            new byte[] {
                3, 86, -107, -76, -52, 75, 9, 65, -26, 5, 81, -41, -95, -100, -13, 6, 3, -37, 91,
                -4, 35, -27, -84, 67, -91, 111, 87, -14, 95, 117, 72, 106
            };
    private static final String BASE64_2 = "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=";
    private static final byte[] BASE64_BYTES_2 =
            new byte[] {
                42, -115, -101, 86, -96, -2, -100, -39, 77, 96, -66, 68, 19, -68, -73, 33, -45, -89,
                -66, 39, -19, -114, 40, -77, -90, 52, 109, -8, 116, -18, 20, 27
            };
    private static final String BASE64_3 = "DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=";
    private static final byte[] BASE64_BYTES_3 =
            new byte[] {
                15, 32, 14, -120, 95, -14, -98, -105, 62, 37, 118, -74, 96, 1, -127, -47, -80, -94,
                -75, 41, 78, 48, -39, -66, 74, 25, -127, -1, -77, 58, 11, -116
            };

    private static final List<String> BASE64_LIST = Arrays.asList(BASE64_1, BASE64_1);
    private static final Base64String BASE64_WRAPPED =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final List<Base64String> BASE64_WRAPPED_LIST =
            Arrays.asList(BASE64_WRAPPED, BASE64_WRAPPED);

    @Test
    public void testWrapList() {
        assertEquals(BASE64_WRAPPED_LIST, Base64String.wrapList(BASE64_LIST));
    }

    @Test
    public void testUnwrapList() {
        assertEquals(BASE64_LIST, Base64String.unwrapList(BASE64_WRAPPED_LIST));
    }

    @Test
    public void testValidBase64String() {
        final Base64String base64String1 = Base64String.wrap(BASE64_1);
        final Base64String base64String2 = Base64String.wrap(BASE64_2);
        final Base64String base64String3 = Base64String.wrap(BASE64_3);

        assertEquals(BASE64_1, base64String1.toString());
        assertEquals(BASE64_2, base64String2.toString());
        assertEquals(BASE64_3, base64String3.toString());

        assertArrayEquals(BASE64_BYTES_1, base64String1.raw());
        assertArrayEquals(BASE64_BYTES_2, base64String2.raw());
        assertArrayEquals(BASE64_BYTES_3, base64String3.raw());
    }

    @Test
    public void testValidBase64ByteArray() {
        final Base64String base64String1 = Base64String.wrap(BASE64_BYTES_1);
        final Base64String base64String2 = Base64String.wrap(BASE64_BYTES_2);
        final Base64String base64String3 = Base64String.wrap(BASE64_BYTES_3);

        assertEquals(BASE64_1, base64String1.toString());
        assertEquals(BASE64_2, base64String2.toString());
        assertEquals(BASE64_3, base64String3.toString());

        assertArrayEquals(BASE64_BYTES_1, base64String1.raw());
        assertArrayEquals(BASE64_BYTES_2, base64String2.raw());
        assertArrayEquals(BASE64_BYTES_3, base64String3.raw());
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyStringThrows() {
        Base64String.wrap("");
    }

    @Test(expected = RuntimeException.class)
    public void testTooShortStringThrows() {
        Base64String.wrap(BASE64_1.substring(0, 43));
    }

    @Test(expected = RuntimeException.class)
    public void testTooLongStringThrows() {
        Base64String.wrap(BASE64_1 + "m");
    }

    @Test(expected = RuntimeException.class)
    public void testNonValidBase64StringThrows() {
        Base64String.wrap("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqr");
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyByteArrayThrows() {
        Base64String.wrap(new byte[] {});
    }

    @Test(expected = RuntimeException.class)
    public void testTooShortByteArrayThrows() {
        Base64String.wrap(Arrays.copyOf(BASE64_BYTES_1, 31));
    }

    @Test(expected = RuntimeException.class)
    public void testTooLongByteArrayThrows() {
        Base64String.wrap(Arrays.copyOf(BASE64_BYTES_1, 33));
    }
}
