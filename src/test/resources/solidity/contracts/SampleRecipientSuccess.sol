pragma solidity ^0.4.2;

/*
This is an example contract that helps test the functionality of the approveAndCall() functionality of HumanStandardToken.sol.
This one assumes successful receival of approval.
*/
contract SampleRecipientSuccess {
  /* A Generic receiving function for contracts that accept tokens */
  address public from;
  uint256 public value;
  address public tokenContract;
  bytes public extraData;

  event ReceivedApproval(uint256 _value);

  function receiveApproval(address _from, uint256 _value, address _tokenContract, bytes _extraData) {
    from = _from;
    value = _value;
    tokenContract = _tokenContract;
    extraData = _extraData;
    ReceivedApproval(_value);
  }
}
