//in order for experimental ABIEncoderV2 to be “stable” when passing structs as arguments we must
//use at least version 0.4.19 of solidity
pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

contract ComplexStorage {

    struct Foo {
        string id;
        string name;
    }

    struct Bar {
        string id;
        uint data;
    }

    struct Baz {
        string id;
        uint data;
    }

    struct Boz {
        uint data;
        string id;
    }

    struct Nuu {
        Foo foo;
    }

    struct Nar {
        Nuu nuu;
    }

    struct Naz {
        Nar nar;
        uint data;
    }

    Foo foo;
    Bar bar;
    Baz baz;
    Boz boz;
    Nuu nuu;
    Nar nar;
    Naz naz;

    constructor(Foo _foo, Bar _bar) public {
        foo = _foo;
        bar = _bar;
        emit Access(msg.sender, _foo, _bar);
    }

    function setFoo(Foo _toSet) public {
        foo = _toSet;
    }

    function setNaz(Naz _naz) public {
        naz = _naz;
    }

    function setBar(Bar _toSet) public {
        bar = _toSet;
    }

    function setBaz(Baz _toSet) public {
        baz = _toSet;
    }

    function setBoz(Boz _toSet) public {
        boz = _toSet;
    }

    function setNuu(Nuu _toSet) public {
        nuu = _toSet;
    }

    function getFoo() public view returns (Foo) {
        return foo;
    }

    function getFooBar() public view returns (Foo, Bar) {
        return (foo, bar);
    }

    function getFooUint() public view returns (Foo, uint256) {
        return (foo, 1);
    }

    event Access(address indexed _address, Foo _foo, Bar _bar);

}