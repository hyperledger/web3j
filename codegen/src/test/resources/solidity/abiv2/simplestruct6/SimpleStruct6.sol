//in order for experimental ABIEncoderV2 to be “stable” when passing structs as arguments we must
//use at least version 0.4.19 of solidity
pragma solidity ^0.4.19;
pragma experimental ABIEncoderV2;

contract SimpleStruct6 {

    struct Bar {
        uint first;
        uint second;
        string third;
        string fourth;
        uint fifth;
        bool sixth;
    }

    Bar bar;

    constructor(Bar _bar) public {
        bar = _bar;
    }

    function setBar(Bar _toSet) public {
        bar = _toSet;
    }

    function getBar() public returns (Bar _bar) {
        emit Access(msg.sender, _bar);
        return (bar);
    }

    event Access(address indexed _address, Bar _bar);
}