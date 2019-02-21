pragma solidity ^0.4.2;

contract Arrays {

    function fixedReverse(uint[10] input) public pure returns(uint[10] result) {
        uint length = input.length;
        for (uint i = 0; i < length; i++) {
            result[i] = input[length - (i + 1)];
        }
        return result;
    }

    function dynamicReverse(uint[] input) public pure returns (uint[] result) {
        uint length = input.length;
        result = new uint[](length);

        for (uint i = 0; i < length; i++) {
            result[i] = input[length - (i + 1)];
        }
        return result;
    }

    function multiDynamic(uint[2][] input) public pure returns (uint[] result) {
        uint length = input.length;
        result = new uint[](length*2);
        uint resultIndex = 0;
        for (uint i = 0; i < length; i++) {
            result[resultIndex] = (input[i][0]);
            resultIndex++;
            result[resultIndex] = (input[i][1]);
            resultIndex++;
        }
        return result;
    }

    function multiFixed(uint[2][6] input) public pure returns (uint[] result) {
        uint length = input.length;
        result = new uint[](length*2);
        uint resultIndex = 0;
        for (uint i = 0; i < length; i++) {
            result[resultIndex] = (input[i][0]);
            resultIndex++;
            result[resultIndex] = (input[i][1]);
            resultIndex++;
        }
        return result;
    }

    function returnArray() public pure returns (address[]) {
        return new address[](0);
    }
}
