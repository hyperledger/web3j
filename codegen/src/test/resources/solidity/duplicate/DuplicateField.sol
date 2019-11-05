pragma solidity ^0.4.0;

contract DuplicateField {

    string public constant NAME = "A";

    string public constant Name = "B";

    string public constant SYMBOL = "SMBL";

    function name() public pure returns (string) {
        return NAME;
    }

    function decimals() public pure returns (uint8) {
        return 0;
    }

}