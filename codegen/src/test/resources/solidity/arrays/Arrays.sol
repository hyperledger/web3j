pragma solidity ^0.4.2;

contract Arrays {

    function fixedReverse(uint[10] input) returns(uint[10] result) {
        uint length = input.length;
        for (uint i = 0; i < length; i++) {
            result[i] = input[length - (i + 1)];
        }
        return result;
    }

    function dynamicReverse(uint[] input) returns (uint[] result) {
        uint length = input.length;
        result = new uint[](length);

        for (uint i = 0; i < length; i++) {
            result[i] = input[length - (i + 1)];
        }
        return result;
    }

    function returnArray() constant returns (address[]) {
        return new address[](0);
    }
}
