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
package org.web3j.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Hash.sha3;
import static org.web3j.crypto.Hash.sha3String;

public class StructuredDataEncoder {
    public final StructuredData.EIP712Message jsonMessageObject;

    // Matches array declarations like arr[5][10], arr[][], arr[][34][], etc.
    // Doesn't match array declarations where there is a 0 in any dimension.
    // Eg- arr[0][5] is not matched.
    final String arrayTypeRegex = "^([a-zA-Z_$][a-zA-Z_$0-9]*)((\\[([1-9]\\d*)?\\])+)$";
    final Pattern arrayTypePattern = Pattern.compile(arrayTypeRegex);

    // This regex tries to extract the dimensions from the
    // square brackets of an array declaration using the ``Regex Groups``.
    // Eg- It extracts ``5, 6, 7`` from ``[5][6][7]``
    final String arrayDimensionRegex = "\\[([1-9]\\d*)?\\]";
    final Pattern arrayDimensionPattern = Pattern.compile(arrayDimensionRegex);

    // Fields of Entry Objects need to follow a regex pattern
    // Type Regex matches to a valid name or an array declaration.
    final String typeRegex = "^[a-zA-Z_$][a-zA-Z_$0-9]*(\\[([1-9]\\d*)*\\])*$";
    final Pattern typePattern = Pattern.compile(typeRegex);
    // Identifier Regex matches to a valid name, but can't be an array declaration.
    final String identifierRegex = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
    final Pattern identifierPattern = Pattern.compile(identifierRegex);

    public StructuredDataEncoder(String jsonMessageInString) throws IOException, RuntimeException {
        // Parse String Message into object and validate
        this.jsonMessageObject = parseJSONMessage(jsonMessageInString);
    }

    public Set<String> getDependencies(String primaryType) {
        // Find all the dependencies of a type
        HashMap<String, List<StructuredData.Entry>> types = jsonMessageObject.getTypes();
        Set<String> deps = new HashSet<>();

        if (!types.containsKey(primaryType)) {
            return deps;
        }

        List<String> remainingTypes = new ArrayList<>();
        remainingTypes.add(primaryType);

        while (remainingTypes.size() > 0) {
            String structName = remainingTypes.get(remainingTypes.size() - 1);
            remainingTypes.remove(remainingTypes.size() - 1);
            deps.add(structName);

            Iterator itr = types.get(primaryType).iterator();
            while (itr.hasNext()) {
                StructuredData.Entry entry = (StructuredData.Entry) itr.next();
                if (!types.containsKey(entry.getType())) {
                    // Don't expand on non-user defined types
                    continue;
                } else if (deps.contains(entry.getType())) {
                    // Skip types which are already expanded
                    continue;
                } else {
                    // Encountered a user defined type
                    remainingTypes.add(entry.getType());
                }
            }
        }

        return deps;
    }

    public String encodeStruct(String structName) {
        HashMap<String, List<StructuredData.Entry>> types = jsonMessageObject.getTypes();

        String structRepresentation = structName + "(";
        for (StructuredData.Entry entry : types.get(structName)) {
            structRepresentation += String.format("%s %s,", entry.getType(), entry.getName());
        }
        structRepresentation = structRepresentation.substring(0, structRepresentation.length() - 1);
        structRepresentation += ")";

        return structRepresentation;
    }

    public String encodeType(String primaryType) {
        Set<String> deps = getDependencies(primaryType);
        deps.remove(primaryType);

        // Sort the other dependencies based on Alphabetical Order and finally add the primaryType
        List<String> depsAsList = new ArrayList<>(deps);
        Collections.sort(depsAsList);
        depsAsList.add(0, primaryType);

        String result = "";
        for (String structName : depsAsList) {
            result += encodeStruct(structName);
        }

        return result;
    }

    public byte[] typeHash(String primaryType) {
        return Numeric.hexStringToByteArray(sha3String(encodeType(primaryType)));
    }

    public List<Integer> getArrayDimensionsFromDeclaration(String declaration) {
        // Get the dimensions which were declared in Schema.
        // If any dimension is empty, then it's value is set to -1.
        Matcher arrayTypeMatcher = arrayTypePattern.matcher(declaration);
        arrayTypeMatcher.find();
        String dimensionsString = arrayTypeMatcher.group(1);
        Matcher dimensionTypeMatcher = arrayDimensionPattern.matcher(dimensionsString);
        List<Integer> dimensions = new ArrayList<>();
        while (dimensionTypeMatcher.find()) {
            String currentDimension = dimensionTypeMatcher.group(1);
            if (currentDimension == null) {
                dimensions.add(Integer.parseInt("-1"));
            } else {
                dimensions.add(Integer.parseInt(currentDimension));
            }
        }

        return dimensions;
    }

    public List<Pair> getDepthsAndDimensions(Object data, int depth) {
        if (!List.class.isInstance(data)) {
            // Nothing more to recurse, since the data is no more an array
            return new ArrayList<>();
        }

        List<Pair> list = new ArrayList<>();
        List<Object> dataAsArray = (List<Object>) data;
        list.add(new Pair(depth, dataAsArray.size()));
        for (Object subdimensionalData : dataAsArray) {
            list.addAll(getDepthsAndDimensions(subdimensionalData, depth + 1));
        }

        return list;
    }

    public List<Integer> getArrayDimensionsFromData(Object data) throws RuntimeException {
        List<Pair> depthsAndDimensions = getDepthsAndDimensions(data, 0);
        // groupedByDepth has key as depth and value as List(pair(Depth, Dimension))
        Map<Object, List<Pair>> groupedByDepth =
                depthsAndDimensions.stream()
                        .collect(
                                Collectors.groupingBy(
                                        depthDimensionPair -> depthDimensionPair.getFirst()));

        // depthDimensionsMap is aimed to have key as depth and value as List(Dimension)
        Map<Integer, List<Integer>> depthDimensionsMap = new HashMap<>();
        for (Map.Entry<Object, List<Pair>> entry : groupedByDepth.entrySet()) {
            List<Integer> pureDimensions = new ArrayList<>();
            for (Pair depthDimensionPair : entry.getValue()) {
                pureDimensions.add((Integer) depthDimensionPair.getSecond());
            }
            depthDimensionsMap.put((Integer) entry.getKey(), pureDimensions);
        }

        List<Integer> dimensions = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : depthDimensionsMap.entrySet()) {
            Set<Integer> setOfDimensionsInParticularDepth = new TreeSet<>(entry.getValue());
            if (setOfDimensionsInParticularDepth.size() != 1) {
                throw new RuntimeException(
                        String.format(
                                "Depth %d of array data has more than one dimensions",
                                entry.getKey()));
            }
            dimensions.add(setOfDimensionsInParticularDepth.stream().findFirst().get());
        }

        return dimensions;
    }

    public List<Object> flattenMultidimensionalArray(Object data) {
        if (!List.class.isInstance(data)) {
            return new ArrayList<Object>() {
                {
                    add(data);
                }
            };
        }

        List<Object> flattenedArray = new ArrayList<>();
        for (Object arrayItem : (List) data) {
            for (Object otherArrayItem : flattenMultidimensionalArray(arrayItem)) {
                flattenedArray.add(otherArrayItem);
            }
        }

        return flattenedArray;
    }

    public byte[] encodeData(String primaryType, HashMap<String, Object> data)
            throws RuntimeException {
        HashMap<String, List<StructuredData.Entry>> types = jsonMessageObject.getTypes();

        List<String> encTypes = new ArrayList<>();
        List<Object> encValues = new ArrayList<>();

        // Add typehash
        encTypes.add("bytes32");
        encValues.add(typeHash(primaryType));

        // Add field contents
        for (StructuredData.Entry field : types.get(primaryType)) {
            Object value = data.get(field.getName());

            if (field.getType().equals("string")) {
                encTypes.add("bytes32");
                byte[] hashedValue = Numeric.hexStringToByteArray(sha3String((String) value));
                encValues.add(hashedValue);
            } else if (field.getType().equals("bytes")) {
                encTypes.add("bytes32");
                byte[] hashedValue = sha3((byte[]) value);
                encValues.add(hashedValue);
            } else if (types.containsKey(field.getType())) {
                // User Defined Type
                byte[] hashedValue =
                        sha3(encodeData(field.getType(), (HashMap<String, Object>) value));
                encTypes.add("bytes32");
                encValues.add(hashedValue);
            } else if (arrayTypePattern.matcher(field.getType()).find()) {
                String baseTypeName = field.getType().substring(0, field.getType().indexOf('['));
                List<Integer> expectedDimensions =
                        getArrayDimensionsFromDeclaration(field.getType());
                // This function will itself give out errors in case
                // that the data is not a proper array
                List<Integer> dataDimensions = getArrayDimensionsFromData(value);

                if (expectedDimensions.size() != dataDimensions.size()) {
                    // Ex: Expected a 3d array, but got only a 2d array
                    throw new RuntimeException(
                            String.format(
                                    "Array Data %s has dimensions %s, "
                                            + "but expected dimensions are %s",
                                    value.toString(),
                                    dataDimensions.toString(),
                                    expectedDimensions.toString()));
                }
                for (int i = 0; i < expectedDimensions.size(); i++) {
                    if (expectedDimensions.get(i) == -1) {
                        // Skip empty or dynamically declared dimensions
                        continue;
                    }
                    if (expectedDimensions.get(i) != dataDimensions.get(i)) {
                        throw new RuntimeException(
                                String.format(
                                        "Array Data %s has dimensions %s, "
                                                + "but expected dimensions are %s",
                                        value.toString(),
                                        dataDimensions.toString(),
                                        expectedDimensions.toString()));
                    }
                }

                List<Object> arrayItems = flattenMultidimensionalArray(value);
                ByteArrayOutputStream concatenatedArrayEncodingBuffer = new ByteArrayOutputStream();
                for (Object arrayItem : arrayItems) {
                    byte[] arrayItemEncoding =
                            encodeData(baseTypeName, (HashMap<String, Object>) arrayItem);
                    concatenatedArrayEncodingBuffer.write(
                            arrayItemEncoding, 0, arrayItemEncoding.length);
                }
                byte[] concatenatedArrayEncodings = concatenatedArrayEncodingBuffer.toByteArray();
                byte[] hashedValue = sha3(concatenatedArrayEncodings);
                encTypes.add("bytes32");
                encValues.add(hashedValue);
            } else {
                encTypes.add(field.getType());
                encValues.add(value);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < encTypes.size(); i++) {
            Class<Type> typeClazz = (Class<Type>) AbiTypes.getType(encTypes.get(i));

            boolean atleastOneConstructorExistsForGivenParametersType = false;
            // Using the Reflection API to get the types of the parameters
            Constructor[] constructors = typeClazz.getConstructors();
            for (Constructor constructor : constructors) {
                // Check which constructor matches
                try {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    byte[] temp =
                            Numeric.hexStringToByteArray(
                                    TypeEncoder.encode(
                                            typeClazz
                                                    .getDeclaredConstructor(parameterTypes)
                                                    .newInstance(encValues.get(i))));
                    baos.write(temp, 0, temp.length);
                    atleastOneConstructorExistsForGivenParametersType = true;
                    break;
                } catch (IllegalArgumentException
                        | NoSuchMethodException
                        | InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException e) {
                    continue;
                }
            }

            if (!atleastOneConstructorExistsForGivenParametersType) {
                throw new RuntimeException(
                        String.format(
                                "Received an invalid argument for which no constructor"
                                        + " exists for the ABI Class %s",
                                typeClazz.getSimpleName()));
            }
        }
        byte[] result = baos.toByteArray();

        return result;
    }

    public byte[] hashMessage(String primaryType, HashMap<String, Object> data)
            throws RuntimeException {
        return sha3(encodeData(primaryType, data));
    }

    public byte[] hashDomain() throws RuntimeException {
        ObjectMapper oMapper = new ObjectMapper();
        HashMap<String, Object> data =
                oMapper.convertValue(jsonMessageObject.getDomain(), HashMap.class);

        data.put("chainId", ((HashMap<String, Object>) data.get("chainId")).get("value"));
        data.put(
                "verifyingContract",
                ((HashMap<String, Object>) data.get("verifyingContract")).get("value"));
        return sha3(encodeData("EIP712Domain", data));
    }

    public void validateStructuredData(StructuredData.EIP712Message jsonMessageObject)
            throws RuntimeException {
        Iterator typesIterator = jsonMessageObject.getTypes().keySet().iterator();
        while (typesIterator.hasNext()) {
            String structName = (String) typesIterator.next();
            List<StructuredData.Entry> fields = jsonMessageObject.getTypes().get(structName);
            Iterator<StructuredData.Entry> fieldsIterator = fields.iterator();
            while (fieldsIterator.hasNext()) {
                StructuredData.Entry entry = fieldsIterator.next();
                if (!identifierPattern.matcher(entry.getName()).find()) {
                    // raise Error
                    throw new RuntimeException(
                            String.format(
                                    "Invalid Identifier %s in %s", entry.getName(), structName));
                }
                if (!typePattern.matcher(entry.getType()).find()) {
                    // raise Error
                    throw new RuntimeException(
                            String.format("Invalid Type %s in %s", entry.getType(), structName));
                }
            }
        }
    }

    public StructuredData.EIP712Message parseJSONMessage(String jsonMessageInString)
            throws IOException, RuntimeException {
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to EIP712Message object
        StructuredData.EIP712Message tempJSONMessageObject =
                mapper.readValue(jsonMessageInString, StructuredData.EIP712Message.class);
        validateStructuredData(tempJSONMessageObject);

        return tempJSONMessageObject;
    }

    public byte[] hashStructuredData() throws RuntimeException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final String messagePrefix = "\u0019\u0001";
        byte[] prefix = messagePrefix.getBytes();
        baos.write(prefix, 0, prefix.length);

        byte[] domainHash = hashDomain();
        baos.write(domainHash, 0, domainHash.length);

        byte[] dataHash =
                hashMessage(
                        jsonMessageObject.getPrimaryType(),
                        (HashMap<String, Object>) jsonMessageObject.getMessage());
        baos.write(dataHash, 0, dataHash.length);

        byte[] result = baos.toByteArray();
        return sha3(result);
    }
}
