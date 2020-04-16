package org.web3j.abi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbiV2Test {
    static List<AbiTestCase> allTests;

    @BeforeAll
    public static void setUpOnce() throws URISyntaxException, IOException {
        String testsAsString =
                new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(AbiV2Test.class
                        .getClassLoader()
                        .getResource("java/org/web3j/abi/contract-interface-abi2.json")).toURI())));
        allTests = Arrays.asList(new ObjectMapper().readValue(testsAsString, AbiTestCase[].class));
    }

    @Test
    public void testAbiv2Encoding() {
        // generate wrapper from abi, encode constructor, assert that expected matches real

    }

}


class AbiTestCase {
    public String bytecode;
    public String result;
    @JsonProperty(value = "interface")
    public String _interface;
    public String name;
    public String runtimeBytecode;
    public String source;
    public String types;
    public String values;
    public String version;

    @Override
    public String toString() {
        return "AbiTestCase{" +
                "bytecode='" + bytecode + '\'' +
                ", result='" + result + '\'' +
                ", _interface='" + _interface + '\'' +
                ", name='" + name + '\'' +
                ", runtimeBytecode='" + runtimeBytecode + '\'' +
                ", source='" + source + '\'' +
                ", types='" + types + '\'' +
                ", values='" + values + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}