pragma solidity ^0.4.0;

contract Revert {

    uint number;

    function set(uint _number) public {
        require(_number != 1);
        require(_number != 2, "The reason for revert");
        number = _number;
    }

    function get() public view returns (uint) {
        return number;
    }

}
