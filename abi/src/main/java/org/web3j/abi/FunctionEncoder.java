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
package org.web3j.abi;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.spi.FunctionEncoderProvider;
import org.web3j.crypto.Hash;
import org.web3j.utils.Collection;
import org.web3j.utils.Numeric;

import static org.web3j.abi.TypeDecoder.instantiateType;
import static org.web3j.abi.TypeReference.makeTypeReference;

/**
 * Delegates to {@link DefaultFunctionEncoder} unless a {@link FunctionEncoderProvider} SPI is
 * found, in which case the first implementation found will be used.
 *
 * @see DefaultFunctionEncoder
 * @see FunctionEncoderProvider
 */
public abstract class FunctionEncoder {

    private static FunctionEncoder DEFAULT_ENCODER;

    private static final ServiceLoader<FunctionEncoderProvider> loader =
            ServiceLoader.load(FunctionEncoderProvider.class);

    public static String encode(final Function function) {
        return encoder().encodeFunction(function);
    }

    public static String encodeConstructor(final List<Type> parameters) {
        return encoder().encodeParameters(parameters);
    }

    public static Function makeFunction(
            String fnname,
            List<String> solidityInputTypes,
            List<Object> arguments,
            List<String> solidityOutputTypes)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
                    IllegalAccessException, InvocationTargetException {
        List<Type> encodedInput = new ArrayList<>();
        Iterator argit = arguments.iterator();
        for (String st : solidityInputTypes) {
            encodedInput.add(instantiateType(st, argit.next()));
        }
        List<TypeReference<?>> encodedOutput = new ArrayList<>();
        for (String st : solidityOutputTypes) {
            encodedOutput.add(makeTypeReference(st));
        }
        return new Function(fnname, encodedInput, encodedOutput);
    }

    protected abstract String encodeFunction(Function function);

    protected abstract String encodeParameters(List<Type> parameters);

    protected static String buildMethodSignature(
            final String methodName, final List<Type> parameters) {

        final StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        final String params = Collection.join(parameters, ",", Type::getTypeAsString);
        result.append(params);
        result.append(")");
        return result.toString();
    }

    protected static String buildMethodId(final String methodSignature) {
        final byte[] input = methodSignature.getBytes();
        final byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    private static FunctionEncoder encoder() {
        final Iterator<FunctionEncoderProvider> iterator = loader.iterator();
        return iterator.hasNext() ? iterator.next().get() : defaultEncoder();
    }

    private static FunctionEncoder defaultEncoder() {
        if (DEFAULT_ENCODER == null) {
            DEFAULT_ENCODER = new DefaultFunctionEncoder();
        }
        return DEFAULT_ENCODER;
    }
}
