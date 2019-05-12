pragma solidity ^0.4.0;

contract SimpleStorage2 {
    uint storedData;
    string storedData2;

    constructor() public {
        storedData = 5;
        storedData2 = "Hello";
    }

    function setInt(uint x) public {
        storedData = x;
    }

    function getInt() public view returns (uint retVal) {
        return storedData;
    }

     function setString(string x) public {
            storedData2 = x;
        }

        function getString() public view returns (string retVal) {
            return storedData2;
        }

        function getBoth() public view returns (uint, string) {
            return (storedData, storedData2);
        }
}
