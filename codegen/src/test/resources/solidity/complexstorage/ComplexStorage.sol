//in order for experimental ABIEncoderV2 to be “stable” when passing structs as arguments we must
//use at least version 0.4.19 of solidity
pragma solidity ^0.6.5;
pragma experimental ABIEncoderV2;

contract ComplexStorage {

    struct Foo {
        string id;
        string name;
    }

    struct Bar {
        uint id;
        uint data;
    }

    struct Fuzz {
        Bar bar;
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

    struct Wiz {
        Foo foo;
        string data;
    }

    struct Fizz {
        Fuzz fuzz;
        Foo foo;
        string data;
    }

    Foo foo;
    Bar bar;
    Fuzz fuzz;
    Baz baz;
    Boz boz;
    Nuu nuu;
    Naz naz;
    Nar nar;
    Wiz wiz;
    Fizz fizz;
    Foo[] multipleFoo;
    Foo[3] staticMultipleFoo;
    Foo[1] foo1;
    Foo[2] foo2;
    Nar[] multipleDynamicNar;
    Nar[3] multipleStaticNar;
    Bar[3] staticMultipleBar;

    constructor() public {
        Foo memory _foo = Foo("id", "name");
        Bar memory _bar = Bar(123,123);
        foo = _foo;
        bar = _bar;

        multipleFoo.push(_foo);
        staticMultipleFoo[0] = _foo;
        staticMultipleFoo[1] = _foo;
        staticMultipleFoo[2] = _foo;
        foo1[0] = _foo;
        foo2[0] = _foo;
        foo2[1] = Foo("id2", "name2");

        staticMultipleBar[1] = _bar;

        nar = Nar(Nuu(Foo("4", "nestedFoo")));
        fizz = Fizz(
            fuzz,
            Foo("id", "name"),
            "fizz Data");

        multipleDynamicNar.push(nar);
        multipleDynamicNar.push(nar);
        multipleDynamicNar.push(Nar(Nuu(Foo("", ""))));
        multipleStaticNar[0] = nar;
        multipleStaticNar[2] = nar;

        emit Access(msg.sender, _foo, _bar);
        emit StructArraysAccess(msg.sender, multipleFoo, staticMultipleBar);
    }

    function setFoo(Foo memory _toSet) public {
        foo = _toSet;
    }

    function setBar(Bar memory _toSet) public {
        bar = _toSet;
    }

    function setFuzz(Fuzz memory _toSet) public {
        fuzz = _toSet;
    }

    function setBaz(Baz memory _toSet) public {
        baz = _toSet;
    }

    function setBoz(Boz memory _toSet) public {
        boz = _toSet;
    }

    function setNuu(Nuu memory _toSet) public {
        nuu = _toSet;
    }

    function setNaz(Naz memory _naz) public {
        naz = _naz;
    }

    function setWiz(Wiz memory _toSet) public {
        wiz = _toSet;
    }

    function getFoo() public view returns (Foo memory) {
        return foo;
    }

    function getBar() public view returns (Bar memory) {
        return bar;
    }

    function getFuzz() public view returns (Fuzz memory) {
        return fuzz;
    }

    function getNaz() public view returns (Naz memory) {
        return naz;
    }

    function getWiz() public view returns (Wiz memory) {
        return wiz;
    }

    function getFooBar() public view returns (Foo memory, Bar memory) {
        return (foo, bar);
    }

    function getFooBarNaz() public view returns (Foo memory, Bar memory, Naz memory) {
        return (foo, bar, naz);
    }

    function getFooUint() public view returns (Foo memory, uint256) {
        return (foo, 1);
    }

    function getMultipleFoo() public view returns (Foo[] memory) {
        return multipleFoo;
    }

    function getEmpty3Foo() public pure returns (Foo[3] memory) {
        Foo[3] memory emptyFoo;
        return emptyFoo;
    }

    function getStaticMultipleFoo() public view returns (Foo[3] memory) {
        return staticMultipleFoo;
    }

    function getFoo1Foo2() public view returns (Foo[1] memory, Foo[2] memory) {
        return (foo1, foo2);
    }

    function idNarBarFooArrays(Nar[3] memory a, Bar[3] memory b, Foo[] memory c, Nar[] memory d, Foo[3] memory e) public pure returns (Nar[3] memory, Bar[3] memory, Foo[] memory, Nar[] memory, Foo[3] memory) {
        return (a, b, c, d, e);
    }

    function getNarBarFooArrays() public view returns (Nar[3] memory, Bar[3] memory, Foo[] memory, Nar[] memory, Foo[3] memory) {
        return (multipleStaticNar, staticMultipleBar, multipleFoo, multipleDynamicNar, staticMultipleFoo);
    }

    event Access(address indexed _address, Foo _foo, Bar _bar);

    event StructArraysAccess(address indexed _address, Foo[] _fooArray, Bar[3] _barArray);
}