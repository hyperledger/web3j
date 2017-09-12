pragma solidity ^0.4.0;

contract SimpleStorage {
    uint storedData;

    function SimpleStorage() {
        storedData = 5;
    }

    function set(uint x) {
        storedData = x;
    }

    function get() constant returns (uint retVal) {
        return storedData;
    }
}
