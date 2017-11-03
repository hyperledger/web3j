Using Infura with web3j
=======================

Signing up
----------

The `Infura <https://infura.io/>`_ service by `ConsenSys <https://consensys.net/>`_, provides
Ethereum clients running in the cloud, so you don't have to run one yourself to work with Ethereum.

When you sign up to the service you are provided with a token you can use to connect to the
relevant Ethereum network:

Main Ethereum Network:
  https://mainnet.infura.io/<your-token>

Test Ethereum Network (Rinkeby):
  https://rinkeby.infura.io/<your-token>

Test Ethereum Network (Kovan):
  https://kovan.infura.io/<your-token>

Test Ethereum Network (Ropsten):
  https://ropsten.infura.io/<your-token>


For obtaining ether to use in these networks, you can refer to :ref:`ethereum-testnets`


InfuraHttpClient
----------------

The web3j infura module provides an Infura HTTP client
(`InfuraHttpService <https://github.com/web3j/web3j/blob/master/infura/src/main/java/org/web3j/protocol/infura/InfuraHttpService.java>`_)
which provides support for the Infura specific *Infura-Ethereum-Preferred-Client* header. This
allows you to specify whether you want a Geth or Parity client to respond to your request. You
can create the client just like the regular HTTPClient::

   Web3j web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/<your-token>"));
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
   System.out.println(web3ClientVersion.getWeb3ClientVersion());

.. code-block:: bash

   Geth/v1.7.2-stable-1db4ecdc/darwin-amd64/go1.9.1

If you want to test a number of the JSON-RPC calls against Infura, update the integration test
`CoreIT <https://github.com/web3j/web3j/blob/master/integration-tests/src/test/java/org/web3j/protocol/core/CoreIT.java>`_
with your Infura URL & run it.

For further information, refer to the
`Infura docs <https://github.com/INFURA/infura/blob/master/docs/source/index.html.md#choosing-a-client-to-handle-your-request>`_.


Transactions
------------

In order to transact with Infura nodes, you will need to create and sign transactions offline
before sending them, as Infura nodes have no visibility of your encrypted Ethereum key files, which
are required to unlock accounts via the Personal Geth/Parity admin commands.

Refer to the :ref:`offline-signing` and :doc:`management_apis` sections for further details.
