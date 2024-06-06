// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.20;

library Foo {
    struct Info {
        bool dummy;
    }
}

library Bar {
    struct Info {
        bool dummy;
    }
}
contract SameInnerStructName {

    constructor() {

    }
    function test(Foo.Info calldata foo, Bar.Info calldata bar) external {
        revert();
    }
}
