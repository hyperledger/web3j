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
import java8.util.function.Function;

/** Utility functions for working with Collections. */
public class Collection {

    static String[] EMPTY_STRING_ARRAY = {};

    private Collection() {}

    public static String[] tail(String[] args) {
        if (args.length == 0) {
            return EMPTY_STRING_ARRAY;
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }

    @SafeVarargs
    public static <T> T[] create(T... args) {
        return args;
    }

    public static <T> String join(List<T> list, String separator, Function<T, String> function) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += function.apply(list.get(i)).trim();
            if (i + 1 < list.size()) {
                result += separator;
            }
        }
        return result;
    }
}
