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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private static final String BASE64_4 =
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE+NYbGgkOxL7LWJh/68eCsHMOvxydvNO4gck/0KFsoCtkyCS3O7TuYfWwk9tr/2WKAsd3/v1a+XGffwnahB1PEw==";
    private static final byte[] BASE64_BYTES_4 =
            new byte[] {
                48, 89, 48, 19, 6, 7, 42, -122, 72, -50, 61, 2, 1, 6, 8, 42, -122, 72, -50, 61, 3,
                1, 7, 3, 66, 0, 4, -8, -42, 27, 26, 9, 14, -60, -66, -53, 88, -104, 127, -21, -57,
                -126, -80, 115, 14, -65, 28, -99, -68, -45, -72, -127, -55, 63, -48, -95, 108, -96,
                43, 100, -56, 36, -73, 59, -76, -18, 97, -11, -80, -109, -37, 107, -1, 101, -118, 2,
                -57, 119, -2, -3, 90, -7, 113, -97, 127, 9, -38, -124, 29, 79, 19
            };

    private static final List<String> BASE64_LIST = Arrays.asList(BASE64_1, BASE64_1);
    private static final List<String> BASE64_4_LIST = Arrays.asList(BASE64_4, BASE64_4);
    private static final Base64String BASE64_WRAPPED =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final Base64String BASE64_4_WRAPPED = Base64String.wrap(BASE64_4);
    private static final List<Base64String> BASE64_WRAPPED_LIST =
            Arrays.asList(BASE64_WRAPPED, BASE64_WRAPPED);
    private static final List<Base64String> BASE64_4_WRAPPED_LIST =
            Arrays.asList(BASE64_4_WRAPPED, BASE64_4_WRAPPED);

    @Test
    public void testWrapList() {
        assertEquals(BASE64_WRAPPED_LIST, Base64String.wrapList(BASE64_LIST));
        assertEquals(BASE64_4_WRAPPED_LIST, Base64String.wrapList(BASE64_4_LIST));
        assertEquals(BASE64_4_WRAPPED_LIST, Base64String.wrapList(BASE64_4, BASE64_4));
    }

    @Test
    public void testUnwrapList() {
        assertEquals(BASE64_LIST, Base64String.unwrapList(BASE64_WRAPPED_LIST));
        assertEquals(BASE64_4_LIST, Base64String.unwrapList(BASE64_4_WRAPPED_LIST));
    }

    @Test
    public void testValidBase64String() {
        final Base64String base64String1 = Base64String.wrap(BASE64_1);
        final Base64String base64String2 = Base64String.wrap(BASE64_2);
        final Base64String base64String3 = Base64String.wrap(BASE64_3);
        final Base64String base64String4 = Base64String.wrap(BASE64_4);

        assertEquals(BASE64_1, base64String1.toString());
        assertEquals(BASE64_2, base64String2.toString());
        assertEquals(BASE64_3, base64String3.toString());
        assertEquals(BASE64_4, base64String4.toString());

        assertArrayEquals(BASE64_BYTES_1, base64String1.raw());
        assertArrayEquals(BASE64_BYTES_2, base64String2.raw());
        assertArrayEquals(BASE64_BYTES_3, base64String3.raw());
        assertArrayEquals(BASE64_BYTES_4, base64String4.raw());
    }

    @Test
    public void testValidBase64ByteArray() {
        final Base64String base64String1 = Base64String.wrap(BASE64_BYTES_1);
        final Base64String base64String2 = Base64String.wrap(BASE64_BYTES_2);
        final Base64String base64String3 = Base64String.wrap(BASE64_BYTES_3);
        final Base64String base64String4 = Base64String.wrap(BASE64_BYTES_4);

        assertEquals(BASE64_1, base64String1.toString());
        assertEquals(BASE64_2, base64String2.toString());
        assertEquals(BASE64_3, base64String3.toString());
        assertEquals(BASE64_4, base64String4.toString());

        assertArrayEquals(BASE64_BYTES_1, base64String1.raw());
        assertArrayEquals(BASE64_BYTES_2, base64String2.raw());
        assertArrayEquals(BASE64_BYTES_3, base64String3.raw());
        assertArrayEquals(BASE64_BYTES_4, base64String4.raw());
    }

    @Test
    public void testEmptyStringThrows() {

        assertThrows(RuntimeException.class, () -> Base64String.wrap(""));
    }

    @Test
    public void testTooLongStringThrows() {

        assertThrows(RuntimeException.class, () -> Base64String.wrap(BASE64_1 + "m"));
    }

    @Test
    public void testNonValidBase64StringThrows() {

        assertThrows(
                RuntimeException.class,
                () -> Base64String.wrap("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqr"));
    }

    @Test
    public void testEmptyByteArrayThrows() {

        assertThrows(RuntimeException.class, () -> Base64String.wrap(new byte[] {}));
    }
}
