pragma solidity ^0.8.0;

contract EventParametersNoNamed {
    event ContractCreated(address, uint256 indexed _contractNumber, string indexed, address);
}
