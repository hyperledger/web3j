Using Nodesmith with web3j
==========================

Getting started
----------

The `Nodesmith <https://nodesmith.io/>`_ service provides hosted access to the Ethereum JSON RPC API
so you don't have to run one yourself to work with Ethereum.

After signing up, you'll be given an Ethereum API key and endpoints for the mainnet and several test
networks. To connect to a specific network, change the url to specify that network.

The url will follow the pattern
``https://ethereum.nodesmith.io/v1/<network_name>/jsonrpc?apiKey=<your_api_key>``

For obtaining ether to use in these networks, you can refer to :ref:`ethereum-testnets`


NodesmithHttpService
--------------------

The ``hosted-providers`` module contains a ``NodesmithHTTPService`` which provides specific information which is returned
in the headers from the Nodesmith service. Nodesmith's service is rate limited to provide a stable and reliable service.
Using the NodesmithHTTPService you can receive information about your API key's current rate limit status.

See `Rate Limiting <https://beta.docs.nodesmith.io/#/ethereum/rateLimiting>`_ for more info

If you want to test a number of the JSON RPC calls against Nodesmith, update the integration test
`CoreIT <https://github.com/web3j/web3j/blob/master/integration-tests/src/test/java/org/web3j/protocol/core/CoreIT.java>`_
with your Nodesmith URL & run it.

Sending Transactions
--------------------

Because Nodesmith doesn't hold any private keys, in order to send transactions they need to be created and signed locally.
They are then sent to Nodesmith's servers to be broadcast to the network.

Refer to the :ref:`offline-signing` and :doc:`management_apis` sections for further details.
