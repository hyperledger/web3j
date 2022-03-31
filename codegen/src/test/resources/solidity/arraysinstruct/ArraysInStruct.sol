// SPDX-License-Identifier: GPL-3.0
pragma solidity >= 0.8.7;

// Test 'Struct' functionality. (from solidity ^0.5.0 )
contract ArraysInStruct {

    struct Entity {
        bytes bytesField;
        bytes32 extraData;

        //Dynamic array size
        string[] stringArrayField;
        bytes[] bytesArrayField;
        bytes2[] bytes2ArrayField;
        bytes32[] bytes32ArrayField;
        uint[] unitArrayField;
        uint256[] unit256ArrayField;
        bool[] boolField;
        int[] intArrayField;
        address[] addressArrayField;


        //Static array size
        string[5] stringArrayFieldStatic;
        bytes[5] bytesArrayFieldStatic;
        bytes2[5] bytes2ArrayFieldStatic;
        bytes32[5] bytes32ArrayFieldStatic;
        uint[5] unitArrayFieldStatic;
        uint256[5] unit256ArrayFieldStatic;
        bool[5] boolFieldStatic;
        int[5] intArrayFieldStatic;
        address[5] addressArrayFieldStatic;
    }

    // Test function
    function callFunction(bytes[] memory bytesArrayField, Entity memory newEntity) public {
        //Functionality is not important here
    }

}