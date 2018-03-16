pragma solidity ^0.4.18;

contract Token {
    mapping (address => uint) public balances;

    event Transfer(address indexed _from, address indexed _to, uint256 _value);

    function Token() {
        balances[msg.sender] = 10000;
    }

    function getBalance(address account) public constant returns (uint balance) {
        return balances[account];
    }

    function transfer(address _to, uint256 _value) public returns (bool success) {
        if (balances[msg.sender] >= _value && _value > 0) {
            balances[msg.sender] -= _value;
            balances[_to] += _value;
            Transfer(msg.sender, _to, _value);
            return true;
        } else { 
            return false; 
        }
    }
}