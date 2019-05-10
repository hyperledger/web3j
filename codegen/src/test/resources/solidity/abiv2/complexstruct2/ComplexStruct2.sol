//in order for experimental ABIEncoderV2 to be “stable” when passing structs as arguments we must
//use at least version 0.4.19 of solidity
pragma solidity ^0.4.19;
pragma experimental ABIEncoderV2;

contract ComplexStruct2 {

    struct HomeAddress {
        uint streetNumber;
        string streetName;
        string suburb;
    }

    struct ContactNumbers {
        uint contactNumberId;
        PhoneNumber contactNumber1;
        PhoneNumber contactNumber2;
    }

    struct PhoneNumber {
        string phoneNumberType; // e.g. Mobile, Home
        AreaCodeAndNumber phoneNumber;
    }

    struct AreaCodeAndNumber {
        uint countryCode;
        string areaCode;
        string theNumber;
    }

    struct Person {
        string firstName;
        string secondName;
        HomeAddress homeAddress;
        ContactNumbers contactNumbers;
    }

    Person person;

    constructor(Person _p) public {
        person = _p;
    }

    function setPerson(Person _toSet) public {
        person = _toSet;
    }

    function getPerson() public returns (Person _p) {
        emit Access(msg.sender, _p);
        return (person);
    }

    event Access(address indexed _address, Person _p);
}