pragma solidity ^0.4.18;

contract SimpleStorage {
    uint storedData;
    address[5] addr;

    function set(uint x, address[5] a) public {
        storedData = x;
        addr = a;
    }

    function get() view public returns (uint, address[5]) {
        return (storedData, addr);
    }
}
