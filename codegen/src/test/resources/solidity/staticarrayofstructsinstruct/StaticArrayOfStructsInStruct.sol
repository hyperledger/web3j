// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.20;


contract StaticArrayOfStructsInStruct {

    struct Player {
        address addr;
        uint256 timeLeft;
    }
    struct Config{
        uint64 index;
        Player[2] players;
    }

    function test(Config calldata config) external {
        revert();
    }
}
