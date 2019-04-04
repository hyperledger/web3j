package org.web3j.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.xml.bind.ValidationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Hash.sha3;
import static org.web3j.crypto.Hash.sha3String;

public class StructuredDataEncoder {
    public static StructuredData.EIP712Message jsonMessageObject;

    public static HashSet<String> getDependencies(String primaryType) {
        // Find all the dependencies of a type
        HashMap<String, Vector<StructuredData.Entry>> types = jsonMessageObject.types;
        HashSet<String> deps = new HashSet<>();

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
                if (!types.containsKey(entry.type)) {
                    // Don't expand on non-user defined types
                    continue;
                } else if (deps.contains(entry.type)) {
                    // Skip types which are already expanded
                    continue;
                } else {
                    // Encountered a user defined type
                    remainingTypes.add(entry.type);
                }
            }
        }

        return deps;
    }

    public static String encodeStruct(String structName) {
        HashMap<String, Vector<StructuredData.Entry>> types = jsonMessageObject.types;

        String structRepresentation = structName + "(";
        for (StructuredData.Entry entry: types.get(structName)) {
            structRepresentation += String.format("%s %s,", entry.type, entry.name);
        }
        structRepresentation = structRepresentation.substring(0, structRepresentation.length() - 1);
        structRepresentation += ")";

        return structRepresentation;
    }

    public static String encodeType(String primaryType) {
        HashSet<String> deps = getDependencies(primaryType);
        deps.remove(primaryType);

        // Sort the other dependencies based on Alphabetical Order and finally add the primaryType
        List<String> depsAsList = new ArrayList<>(deps);
        Collections.sort(depsAsList);
        depsAsList.add(0, primaryType);

        String result = "";
        for (String structName: depsAsList) {
            result += encodeStruct(structName);
        }

        return result;
    }

    public static byte[] typeHash(String primaryType) {
        return Numeric.hexStringToByteArray(sha3String(encodeType(primaryType)));
    }

    public static byte[] encodeData(
            String primaryType,
            LinkedHashMap<String, Object> data) throws Throwable {
        HashMap<String, Vector<StructuredData.Entry>> types = jsonMessageObject.types;

        List<String> encTypes = new ArrayList<>();
        List<Object> encValues = new ArrayList<>();

        // Add typehash
        encTypes.add("bytes32");
        encValues.add(typeHash(primaryType));

        // Add field contents
        for (StructuredData.Entry field: types.get(primaryType)) {
            Object value = data.get(field.name);

            if (field.type.equals("string")) {
                encTypes.add("bytes32");
                byte[] hashedValue = Numeric.hexStringToByteArray(sha3String((String) value));
                encValues.add(hashedValue);
            } else if (field.type.equals("bytes")) {
                encTypes.add("bytes32");
                byte[] hashedValue = sha3((byte[]) value);
                encValues.add(hashedValue);
            } else if (types.containsKey(field.type)) {
                // User Defined Type
                byte[] hashedValue = sha3(
                        encodeData(field.type, (LinkedHashMap<String, Object>) value)
                );
                encTypes.add("bytes32");
                encValues.add(hashedValue);
            } else if (field.type.lastIndexOf(']') == field.type.length() - 1) {
                // Still not implemented
                throw new Throwable("TODO: Arrays currently unimplemented in encodeData");
            } else {
                encTypes.add(field.type);
                encValues.add(value);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < encTypes.size(); i++) {
            Class<Type> typeClazz = (Class<Type>) AbiTypes.getType(encTypes.get(i));

            boolean atleastOneConstructorExistsForGivenParametersType = false;
            // Using the Reflection API to get the types of the parameters
            Constructor[] constructors = typeClazz.getConstructors();
            for (Constructor constructor: constructors) {
                // Check which constructor matches
                try {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    byte[] temp = Numeric.hexStringToByteArray(
                            TypeEncoder.encode(
                                    typeClazz
                                            .getDeclaredConstructor(parameterTypes)
                                            .newInstance(encValues.get(i))
                            )
                    );
                    baos.write(temp, 0, temp.length);
                    atleastOneConstructorExistsForGivenParametersType = true;
                    break;
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }

            if (!atleastOneConstructorExistsForGivenParametersType) {
                throw new ValidationException(
                        String.format(
                                "Received an invalid argument for which no constructor"
                                        + " exists for the ABI Class %s",
                                typeClazz.getSimpleName()
                        )
                );
            }
        }
        byte[] result = baos.toByteArray();

        return result;
    }

    public static byte[] hashMessage(
            String primaryType,
            LinkedHashMap<String, Object> data) throws Throwable {
        return sha3(encodeData(primaryType, data));
    }

    public static byte[] hashDomain() throws Throwable {
        ObjectMapper oMapper = new ObjectMapper();
        LinkedHashMap<String, Object> data = oMapper.convertValue(
                jsonMessageObject.domain,
                LinkedHashMap.class
        );

        data.put(
                "chainId",
                ((LinkedHashMap<String, Object>) data.get("chainId")).get("value")
        );
        data.put(
                "verifyingContract",
                ((LinkedHashMap<String, Object>) data.get("verifyingContract")).get("value")
        );
        return sha3(encodeData("EIP712Domain", data));
    }

    public static void validateStructuredData() throws ValidationException {
        // Fields of Entry Objects need to follow a regex pattern
        String typeRegex = "^[a-zA-Z_$][a-zA-Z_$0-9]*(\\[([1-9]\\d*)*\\])*$";
        String identifierRegex = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
        Iterator typesIterator = jsonMessageObject.types.keySet().iterator();
        while (typesIterator.hasNext()) {
            String structName = (String) typesIterator.next();
            Vector<StructuredData.Entry> fields = jsonMessageObject.types.get(structName);
            Iterator<StructuredData.Entry> fieldsIterator = fields.iterator();
            while (fieldsIterator.hasNext()) {
                StructuredData.Entry entry = fieldsIterator.next();
                if (!Pattern.matches(identifierRegex, entry.name)) {
                    // raise Error
                    throw new ValidationException(
                            String.format(
                                    "Invalid Identifier %s in %s", entry.name, structName
                            )
                    );
                }
                if (!Pattern.matches(typeRegex, entry.type)) {
                    // raise Error
                    throw new ValidationException(
                            String.format(
                                    "Invalid Type %s in %s", entry.type, structName
                            )
                    );
                }
            }
        }
    }

    public static void parseJSONMessage(
            String jsonMessageInString) throws IOException, ValidationException {
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON string to EIP712Message object
        jsonMessageObject = mapper.readValue(
                jsonMessageInString,
                StructuredData.EIP712Message.class
        );

        validateStructuredData();
    }

    public static byte[] hashStructuredData(String jsonMessageInString) throws Throwable {
        // Parse String Message into object and validate
        parseJSONMessage(jsonMessageInString);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final String messagePrefix = "\u0019\u0001";
        byte[] prefix = messagePrefix.getBytes();
        baos.write(prefix, 0, prefix.length);

        byte[] domainHash = hashDomain();
        baos.write(domainHash, 0, domainHash.length);

        byte[] dataHash = hashMessage(
                jsonMessageObject.primaryType,
                (LinkedHashMap<String, Object>) jsonMessageObject.message
        );
        baos.write(dataHash, 0, dataHash.length);

        byte[] result = baos.toByteArray();
        return sha3(result);
    }
}
