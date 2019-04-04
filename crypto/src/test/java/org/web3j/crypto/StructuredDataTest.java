package org.web3j.crypto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void testEncodeData() throws Throwable {
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
    public void testHashData() throws Throwable {
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
    public void testHashDomain() throws Throwable {
        byte[] structHashDomain = StructuredDataEncoder.hashDomain();
        String expectedDomainStructHash = "0xf2cee375fa42b42143804025fc449deafd"
                + "50cc031ca257e0b194a650a912090f";

        assertEquals(
                Numeric.toHexString(structHashDomain),
                expectedDomainStructHash
        );
    }

    @Test
    public void testHashStructuredMessage() throws Throwable {
        byte[] hashStructuredMessage = StructuredDataEncoder.hashStructuredData(jsonMessageString);
        String expectedDomainStructHash = "0xbe609aee343fb3c4b28e1df9e632fca64fcfaede20"
                + "f02e86244efddf30957bd2";

        assertEquals(
                Numeric.toHexString(hashStructuredMessage),
                expectedDomainStructHash
        );
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidMessageValueTypeMismatch() throws Throwable {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageValueTypeMismatch.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidMessageInvalidABIType() throws Throwable {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageInvalidABIType.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidMessageValidABITypeInvalidValue() throws Throwable {
        String invalidStructuredDataJSONFilePath = "build/resources/test/"
                + "structured_data_json_files/InvalidMessageValidABITypeInvalidValue.json";
        jsonMessageString = new String(
                Files.readAllBytes(Paths.get(invalidStructuredDataJSONFilePath).toAbsolutePath()),
                "UTF-8"
        );
        StructuredDataEncoder.hashStructuredData(jsonMessageString);
    }
}
