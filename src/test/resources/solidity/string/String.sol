pragma solidity ^0.4.2;

contract String {

    string value;

    function simple() constant returns (string) {
        return "one more time";
    }

    function lorem() constant returns (string)  {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

    function empty() constant returns (string) {
        return "";
    }

    function set(string _value) {
        value = _value;
    }

    function get() returns (string) {
        return value;
    }

}
