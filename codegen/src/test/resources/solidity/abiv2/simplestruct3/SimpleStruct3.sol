//in order for experimental ABIEncoderV2 to be “stable” when passing structs as arguments we must
//use at least version 0.5.8 of solidity
pragma solidity ^0.5.8;
pragma experimental ABIEncoderV2;

contract SimpleStruct3 {

    struct Bar {
        uint   intval;
        string data;
    }

    Bar bar;

    constructor(Bar memory _bar) public {
        bar = _bar;
    }

    function setBar(Bar memory _toSet) public {
        bar = _toSet;
    }

    function getBar() public view returns (Bar memory) {
        return (bar);
    }

}