Management APIs
===============

In addition to implementing the standard `JSON-RPC <https://github.com/ethereum/wiki/wiki/JSON-RPC>`_ API, Ethereum clients, such as `Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`__ and `Parity <https://github.com/ethcore/parity>`__ provide additional management via JSON-RPC.

One of the key common pieces of functionality that they provide is the ability to create & unlock Ethereum accounts for transacting on the network. In Geth and Parity, this is implemented in their Personal modules, details of which are available below:

- `Parity <https://github.com/ethcore/parity/wiki/JSONRPC-personal-module>`__
- `Geth <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal>`__

Support for the personal modules is available in web3j. Both clients have further admin commands available, they will be incorporated into web3j over time if there is sufficient demand.

As Parity provides a superset of the personal admin commands offered by Geth, you can initialise a new web3j connector using the `Parity <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/parity/Parity.java>`_ factory method::

   Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
   }

Refer to the integration test `org.web3j.protocol.parity.ParityIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/parity/ParityIT.java>`_ for further implementation details.
