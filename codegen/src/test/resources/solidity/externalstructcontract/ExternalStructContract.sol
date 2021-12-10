//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.4;
pragma experimental ABIEncoderV2;

struct ExternalStruct {
    int id_int;
    string id_ext;
}

contract ExternalStructContract {
    ExternalStruct ext;

    constructor() public {
        ext = ExternalStruct(1, "1");
    }

    function set(int id_int, string memory id_ext) public {
        ext = ExternalStruct(id_int, id_ext);
    }

    function get() public view returns (ExternalStruct memory ext) {
        return ext;
    }
}
