pragma solidity ^0.4.2;
contract ShipIt {

    enum Status{
        SHIPMENT_CREATED
    }

    struct Shipment{
        address sender;
        address receiver;
        uint creationDate;
        uint departureDate;
        Status status;
        uint packageWeight;
        string depot;
        bytes32 receiverHash;
    }

    mapping(address => Shipment) public shipments;
}
