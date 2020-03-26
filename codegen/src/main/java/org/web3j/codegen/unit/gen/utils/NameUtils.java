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
package org.web3j.codegen.unit.gen.utils;

import java.lang.reflect.Type;

public class NameUtils {
    public static String toCamelCase(final Class aClass) {
        return Character.toLowerCase(aClass.getSimpleName().charAt(0))
                + aClass.getSimpleName().substring(1);
    }

    public static String returnTypeAsLiteral(final Type type, final boolean extend) {
        final String formattedString =
                type.getTypeName().substring(type.getTypeName().lastIndexOf(".") + 1);
        return extend
                ? formattedString.replaceAll("[^a-zA-Z0-9]", "") + "Var"
                : formattedString.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String toCamelCase(final String aString) {
        return Character.toLowerCase(aString.charAt(0)) + aString.substring(1);
    }
}
