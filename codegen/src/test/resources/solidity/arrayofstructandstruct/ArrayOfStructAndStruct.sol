// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.20;


contract ArrayOfStructAndStruct {

    struct Foo {
        bool dummy;
    }

    constructor() {

    }
    function test(Foo[2] calldata foos) external {
        revert();
    }

    function testSingle(Foo calldata foo) external {
        revert();
    }
}
