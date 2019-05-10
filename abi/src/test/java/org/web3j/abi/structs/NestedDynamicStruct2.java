package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class NestedDynamicStruct2 extends DynamicStructType {

    public static class HomeAddressStruct extends DynamicStructType {

        public HomeAddressStruct(BigInteger streetNumber, String streetName, String suburb) {
            this(new Uint256(streetNumber), new Utf8String(streetName), new Utf8String(suburb));
        }

        public HomeAddressStruct(Uint256 streetNumber, Utf8String streetName, Utf8String suburb) {
            super(Type.class, Arrays.asList(streetNumber, streetName, suburb));
        }

        public static List<TypeReference> getTypes() {
            return Arrays.asList(
                    new TypeReference<Uint256>() {
                    },
                    new TypeReference<Utf8String>() {
                    },
                    new TypeReference<Utf8String>() {
                    });
        }
    }

    public static class ContactNumbers extends DynamicStructType {

        public ContactNumbers(BigInteger contactNumberId, PhoneNumber contactNumber1, PhoneNumber contactNumber2) {
            this(new Uint256(contactNumberId), contactNumber1, contactNumber2);
        }

        public ContactNumbers(Uint256 contactNumberId, PhoneNumber contactNumber1, PhoneNumber contactNumber2) {
            super(Type.class, Arrays.asList(contactNumberId, contactNumber1, contactNumber2));
        }

        public static List<TypeReference> getTypes() {
            return Arrays.asList(
                    new TypeReference<Uint256>() {
                    },
                    new TypeReference<PhoneNumber>() {
                    },
                    new TypeReference<PhoneNumber>() {
                    });
        }
    }

    public static class PhoneNumber extends DynamicStructType {

        public PhoneNumber(String phoneNumberType, AreaCodeAndNumber areaCodeAndNumber) {
            this(new Utf8String(phoneNumberType), areaCodeAndNumber);
        }

        public PhoneNumber(Utf8String phoneNumberType, AreaCodeAndNumber areaCodeAndNumber) {
            super(Type.class, Arrays.asList(phoneNumberType, areaCodeAndNumber));
        }

        public static List<TypeReference> getTypes() {
            return Arrays.asList(
                    new TypeReference<Utf8String>() {
                    },
                    new TypeReference<AreaCodeAndNumber>() {
                    });
        }
    }

    public static class AreaCodeAndNumber extends DynamicStructType {

        public AreaCodeAndNumber(BigInteger countryCode, String areaCode, String theNumber) {
            this(new Uint256(countryCode), new Utf8String(areaCode), new Utf8String(theNumber));
        }

        public AreaCodeAndNumber(Uint256 countryCode, Utf8String areaCode, Utf8String theNumber) {
            super(Type.class, Arrays.asList(countryCode, areaCode, theNumber));
        }

        public static List<TypeReference> getTypes() {
            return Arrays.asList(
                    new TypeReference<Uint256>() {
                    },
                    new TypeReference<Utf8String>() {
                    },
                    new TypeReference<Utf8String>() {
                    });
        }
    }

    public NestedDynamicStruct2(String firstName, String lastName, HomeAddressStruct homeAddress, ContactNumbers contactNumbers) {
        this(new Utf8String(firstName), new Utf8String(lastName), homeAddress, contactNumbers);
    }

    public NestedDynamicStruct2(Utf8String firstName, Utf8String lastName, HomeAddressStruct homeAddress, ContactNumbers contactNumbers) {
        super(Type.class, Arrays.asList(firstName, lastName, homeAddress, contactNumbers));
    }

    public static List<TypeReference> getTypes() {
        return Arrays.asList(
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<HomeAddressStruct>() {
                },
                new TypeReference<ContactNumbers>() {
                });
    }

}