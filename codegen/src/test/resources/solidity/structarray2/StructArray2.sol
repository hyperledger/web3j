pragma solidity ^0.8.15;

contract StructArray2 {
    event Boo(
        uint256 mom,
        Foo[] fooz
    );

    function getFoo(Foo memory foo) public view returns (uint) {
        return 0;
    }
}

struct Foo {
    Bar[] bars;
}

struct Bar {
    Baz[] baz;
    uint256 mem;
}

struct Baz {
    uint256 fuz;
}