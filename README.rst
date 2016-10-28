.. To build this file locally ensure docutils Python package is installed and run:
   $ rst2html.py README.rst README.html

web3j: Web3 Java Ethereum Ðapp API
==================================

.. image:: https://readthedocs.org/projects/web3j/badge/?version=latest
   :target: http://web3j.readthedocs.io/en/latest/?badge=latest
   :alt: Documentation Status

.. image:: https://travis-ci.org/web3j/web3j.svg?branch=master
   :target: https://travis-ci.org/web3j/web3j
   :alt: Build Status

.. image:: https://codecov.io/gh/web3j/web3j/branch/master/graph/badge.svg
   :target: https://codecov.io/gh/web3j/web3j
   :alt: codecov

.. image:: https://badges.gitter.im/web3j/web3j.svg
   :target: https://gitter.im/web3j/web3j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
   :alt: Join the chat at https://gitter.im/web3j/web3j

web3j is a lightweight, type safe Java library for integrating with clients (nodes) on the Ethereum network::

   [ JVM application ] + [ web3j ] <---> [ Ethereum node ]

It can generate Java smart contract wrappers so you can interact with a smart contract like it's native Java code. 

It only has four runtime dependencies:

- `Apache HTTP Client <https://hc.apache.org/httpcomponents-client-ga/index.html>`_
- `Jackson Core <https://github.com/FasterXML/jackson-core>`_ for fast JSON
  serialisation/deserialisation
- `Bouncy Castle <https://www.bouncycastle.org/>`_ for crypto
- `JavaPoet <https://github.com/square/javapoet>`_ for generating smart contract wrappers

Full project documentation is available at
`Read the Docs <https://web3j.readthedocs.io/en/latest/>`_.


Getting Started
---------------

Add the following dependency to your project:

Maven
-----

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>0.6.0</version>
   </dependency>

Gradle
------

.. code-block:: groovy

   compile ('org.web3j:core:0.6.0')


Start up an Ethereum client if you don't already have one running, such as `Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`_:

.. code-block:: bash

   $ geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

Or `Parity <https://github.com/ethcore/parity>`_:

.. code-block:: bash

   $ parity --chain testnet



To send asynchronous requests using a Future:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();


To send synchronous requests:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();

To use Parity commands:

.. code-block:: java

   Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
   }


Further Details
---------------

- web3j provides type safe access to all responses. Optional or null responses are wrapped in
  Java 8's
  `Optional <https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html>`_ type.
- Async requests are handled using Java 8's
  `CompletableFutures <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html>`_.
- Quantity payload types are returned as `BigIntegers <https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html>`_.
  For simple results, you can obtain the quantity as a String via
  `Response <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/Response.java>`_.getResult().


Generating smart contract wrappers in Java
------------------------------------------

The library also supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

This can be achieved by running:

.. code-block:: bash

   $ org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.abi -o /path/to/src/dir/java -p com.your.organisation.name


See `FunctionWrappersIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/FunctionWrappersIT.java>`_
for an example of using a generated smart contract Java wrapper.


Working with filters
--------------------

See `EventFilterIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/EventFilterIT.java>`_
for an example.


Tested Clients
--------------

Geth

- 1.4.18-stable-ef9265d0

Parity

- Parity/v1.3.8-beta/x86_64-linux-gnu/rustc1.12.0

You can run the integration test class
`CoreIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/core/CoreIT.java>`_
to verify clients.


Coming Soon
-----------

- External key store support
- IPC interface support
- WebSocket interface support


Related Projects
----------------

For a .NET implementation, check out `Nethereum <https://github.com/Nethereum/Nethereum>`_.
 
For a pure Java implementation of the Ethereum client, check out
`EthereumJ <https://github.com/ethereum/ethereumj>`_ and the work of
`Ether.Camp <https://github.com/ether-camp/>`_.


Build Instructions
------------------

web3j includes integration tests for running against a live Ethereum client. If you do not have a
client running, you can exclude their execution as per the below instructions.

To run a full build including integration tests:

.. code-block:: bash

   $ ./gradlew check


To run excluding integration tests:

.. code-block:: bash

   $ ./gradlew -x integrationTest check

Thanks and Credits
------------------

- The `Nethereum <https://github.com/Nethereum/Nethereum>`_ project for the inspiration
- `Othera <https://www.othera.com.au/>`_ for the great things they are building on the platform
- `Finhaus <http://finhaus.com.au/>`_ guys for putting me onto Nethereum
- Everyone involved in the Ethererum project and its surrounding ecosystem
- And of course the users of the library, who've provided valuable input & feedback -
  `@ice09 <https://github.com/ice09>`_, `@adridadou <https://github.com/adridadou>`_,
  `@nickmelis <https://github.com/nickmelis>`_, `@basavk <https://github.com/basavk>`_,
  `@kabl <https://github.com/kabl>`_
