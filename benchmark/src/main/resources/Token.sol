pragma solidity ^0.4.16;

contract Token {
    mapping (address => uint256) public balanceOf;

    event Transfer(
        address from,
        address to,
        uint256 tokens
    );

    /* Initializes contract with initial supply tokens to the creator of the contract */
    function Token(uint256 initialSupply) {
        balanceOf[msg.sender] = initialSupply;
    }

    /* Send coins */
    function transfer(address _to, uint256 _value) {
        require(balanceOf[msg.sender] >= _value);
        require(balanceOf[_to] + _value >= balanceOf[_to]);
        balanceOf[msg.sender] -= _value;
        balanceOf[_to] += _value;
        Transfer(msg.sender, _to, _value);
    }
}
