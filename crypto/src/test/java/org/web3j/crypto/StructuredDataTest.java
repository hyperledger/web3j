package org.web3j.crypto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import javax.xml.bind.ValidationException;

import org.junit.Before;
import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.junit.Assert.assertEquals;

public class StructuredDataTest {

    private static String jsonMessageString;

    @Before
    public void validSetUp() throws IOException, ValidationException {
        String validStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/ValidStructuredData.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(validStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.parseJSONMessage(jsonMessageString);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidIdentifierMessageCaughtByRegex()
            throws IOException, ValidationException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidIdentifierStructuredData.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.parseJSONMessage(jsonMessageString);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeMessageCaughtByRegex() throws IOException, ValidationException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidTypeStructuredData.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.parseJSONMessage(jsonMessageString);
    }

    @Test
    public void testGetDependencies() {
        HashSet<String> deps = StructuredDataEncoder.getDependencies(
                StructuredDataEncoder.jsonMessageObject.primaryType
        );

        HashSet<String> depsExpected = new HashSet<>();
        depsExpected.add("Mail");
        depsExpected.add("Person");

        assertEquals(deps, depsExpected);
    }

    @Test
    public void testEncodeType() {
        String expectedTypeEncoding = "Mail(Person from,Person to,string contents)"
                        + "Person(string name,address wallet)";

        assertEquals(
                StructuredDataEncoder.encodeType(
                        StructuredDataEncoder.jsonMessageObject.primaryType
                ),
                expectedTypeEncoding
        );
    }

    @Test
    public void testTypeHash() {
        String expectedTypeHashHex = "0xa0cedeb2dc280ba39b857546d74f5549c"
                        + "3a1d7bdc2dd96bf881f76108e23dac2";

        assertEquals(
                Numeric.toHexString(
                        StructuredDataEncoder.typeHash(
                                StructuredDataEncoder.jsonMessageObject.primaryType
                        )
                ),
                expectedTypeHashHex
        );
    }

    @Test
    public void testEncodeData() throws ValidationException {
        byte[] encodedData = StructuredDataEncoder.encodeData(
                StructuredDataEncoder.jsonMessageObject.primaryType,
                (LinkedHashMap<String, Object>) StructuredDataEncoder.jsonMessageObject.message);
        String expectedDataEncodingHex = "0xa0cedeb2dc280ba39b857546d74f5549c3a1d7bd"
                + "c2dd96bf881f76108e23dac2fc71e5fa27ff56c350aa531bc129ebdf613b772b6"
                + "604664f5d8dbe21b85eb0c8cd54f074a4af31b4411ff6a60c9719dbd559c221c8"
                + "ac3492d9d872b041d703d1b5aadf3154a261abdd9086fc627b61efca26ae57027"
                + "01d05cd2305f7c52a2fc8";

        assertEquals(
                Numeric.toHexString(encodedData),
                expectedDataEncodingHex
        );
    }

    @Test
    public void testHashData() throws ValidationException {
        byte[] dataHash = StructuredDataEncoder.hashMessage(
                StructuredDataEncoder.jsonMessageObject.primaryType,
                (LinkedHashMap<String, Object>) StructuredDataEncoder.jsonMessageObject.message
        );
        String expectedMessageStructHash = "0xc52c0ee5d84264471806290a3f2c4cecf"
                + "c5490626bf912d01f240d7a274b371e";

        assertEquals(
                Numeric.toHexString(dataHash),
                expectedMessageStructHash
        );
    }

    @Test
    public void testHashDomain() throws ValidationException {
        byte[] structHashDomain = StructuredDataEncoder.hashDomain();
        String expectedDomainStructHash = "0xf2cee375fa42b42143804025fc449deafd"
                + "50cc031ca257e0b194a650a912090f";

        assertEquals(
                Numeric.toHexString(structHashDomain),
                expectedDomainStructHash
        );
    }

    @Test
    public void testHashStructuredMessage() throws ValidationException, IOException {
        byte[] hashStructuredMessage = StructuredDataEncoder.hashStructuredData(jsonMessageString);
        String expectedDomainStructHash = "0xbe609aee343fb3c4b28e1df9e632fca64fcfaede20"
                + "f02e86244efddf30957bd2";

        assertEquals(
                Numeric.toHexString(hashStructuredMessage),
                expectedDomainStructHash
        );
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidMessageValueTypeMismatch() throws ValidationException, IOException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageValueTypeMismatch.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidMessageInvalidABIType() throws ValidationException, IOException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageInvalidABIType.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidMessageValidABITypeInvalidValue()
            throws ValidationException, IOException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageValidABITypeInvalidValue.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }


    @Test
    public void testGetArrayDimensionsFromData() throws ValidationException {
        // [[1, 2, 3], [4, 5, 6]]
        ArrayList<Object> testArrayData1 = new ArrayList<>();
        testArrayData1.add(new ArrayList<>(Arrays.asList("1", "2", "3")));
        testArrayData1.add(new ArrayList<>(Arrays.asList("4", "5", "6")));
        ArrayList<Integer> expectedDimensions1 = new ArrayList<Integer>() {
            {
                add(2);
                add(3);
            }
        };
        assertEquals(
                StructuredDataEncoder.getArrayDimensionsFromData(testArrayData1),
                expectedDimensions1
        );

        // [[1, 2, 3]]
        ArrayList<Object> testArrayData2 = new ArrayList<>();
        testArrayData2.add(new ArrayList<>(Arrays.asList("1", "2", "3")));
        ArrayList<Integer> expectedDimensions2 = new ArrayList<Integer>() {
            {
                add(1);
                add(3);
            }
        };
        assertEquals(
                StructuredDataEncoder.getArrayDimensionsFromData(testArrayData2),
                expectedDimensions2
        );

        // [1, 2, 3]
        ArrayList<Object> testArrayData3 = new ArrayList<Object>() {
            {
                add("1");
                add("2");
                add("3");
            }
        };
        ArrayList<Integer> expectedDimensions3 = new ArrayList<Integer>() {
            {
                add(3);
            }
        };
        assertEquals(
                StructuredDataEncoder.getArrayDimensionsFromData(testArrayData3),
                expectedDimensions3
        );

        // [[[1, 2], [3, 4], [5, 6]], [[7, 8], [9, 10], [11, 12]]]
        ArrayList<Object> testArrayData4 = new ArrayList<>();
        testArrayData4.add(
                new ArrayList<Object>() {
                    {
                        add(new ArrayList<>(Arrays.asList("1", "2")));
                        add(new ArrayList<>(Arrays.asList("3", "4")));
                        add(new ArrayList<>(Arrays.asList("5", "6")));
                    }
                }
        );
        testArrayData4.add(
                new ArrayList<Object>() {
                    {
                        add(new ArrayList<>(Arrays.asList("7", "8")));
                        add(new ArrayList<>(Arrays.asList("9", "10")));
                        add(new ArrayList<>(Arrays.asList("11", "12")));
                    }
                }
        );
        ArrayList<Integer> expectedDimensions4 = new ArrayList<Integer>() {
            {
                add(2);
                add(3);
                add(2);
            }
        };
        assertEquals(
                StructuredDataEncoder.getArrayDimensionsFromData(testArrayData4),
                expectedDimensions4
        );
    }

    @Test
    public void testFlattenMultidimensionalArray() {
        // [[1, 2, 3], [4, 5, 6]]
        ArrayList<Object> testArrayData1 = new ArrayList<>();
        testArrayData1.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        testArrayData1.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        ArrayList<Integer> expectedFlatArray1 = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
            }
        };
        assertEquals(
                StructuredDataEncoder.flattenMultidimensionalArray(testArrayData1),
                expectedFlatArray1
        );

        // [[1, 2, 3]]
        ArrayList<Object> testArrayData2 = new ArrayList<>();
        testArrayData2.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        ArrayList<Integer> expectedFlatArray2 = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };
        assertEquals(
                StructuredDataEncoder.flattenMultidimensionalArray(testArrayData2),
                expectedFlatArray2
        );

        // [1, 2, 3]
        ArrayList<Object> testArrayData3 = new ArrayList<Object>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };
        ArrayList<Integer> expectedFlatArray3 = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };
        assertEquals(
                StructuredDataEncoder.flattenMultidimensionalArray(testArrayData3),
                expectedFlatArray3
        );

        // [[[1, 2], [3, 4], [5, 6]], [[7, 8], [9, 10], [11, 12]]]
        ArrayList<Object> testArrayData4 = new ArrayList<>();
        testArrayData4.add(
                new ArrayList<Object>() {
                    {
                        add(new ArrayList<>(Arrays.asList(1, 2)));
                        add(new ArrayList<>(Arrays.asList(3, 4)));
                        add(new ArrayList<>(Arrays.asList(5, 6)));
                    }
                }
        );
        testArrayData4.add(
                new ArrayList<Object>() {
                    {
                        add(new ArrayList<>(Arrays.asList(7, 8)));
                        add(new ArrayList<>(Arrays.asList(9, 10)));
                        add(new ArrayList<>(Arrays.asList(11, 12)));
                    }
                }
        );
        ArrayList<Integer> expectedFlatArray4 = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
                add(7);
                add(8);
                add(9);
                add(10);
                add(11);
                add(12);
            }
        };
        assertEquals(
                StructuredDataEncoder.flattenMultidimensionalArray(testArrayData4),
                expectedFlatArray4
        );
    }

    @Test(expected = ValidationException.class)
    public void testUnequalArrayLengthsBetweenSchemaAndData()
            throws IOException, ValidationException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/"
                + "InvalidMessageUnequalArrayLengthsBetweenSchemaAndData.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }

    @Test(expected = ValidationException.class)
    public void testDataNotPerfectArrayButDeclaredArrayInSchema()
            throws IOException, ValidationException {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/"
                + "InvalidMessageDataNotPerfectArrayButDeclaredArrayInSchema.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }
}
