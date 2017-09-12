pragma solidity ^0.4.2;

/*
This is an example contract that helps test the functionality of the approveAndCall() functionality of HumanStandardToken.sol.
This one will throw and thus needs to propagate the error up.
*/
contract SampleRecipientThrow {
  function () {
    throw;
  }
}
