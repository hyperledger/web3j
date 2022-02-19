pragma solidity ^0.8.0;

contract EventParameters{
    uint256 public _contractNumber = 22222;
    address public _testAddress = 0x9A734f85fE7676096979503f8CEd26EA387138b4;

    event TestEvent(address, uint256 indexed _contractNumber, string indexed, address);

    function testEvent(address) public  {
        emit TestEvent(address(this), _contractNumber, "Function: testEvent", _testAddress);
    }
}
