pragma solidity ^0.8.15;

contract StructArray {
    function getBar(Bar[] memory bars) public view returns (uint) {
        return 0;
    }
}

struct Bar {
    uint256 baz;
}