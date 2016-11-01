Getting Started
===============

Add the latest web3j version to your project build configuration.

Maven
-----

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>0.7.0</version>
   </dependency>

Gradle
------

.. code-block:: groovy

   compile ('org.web3j:core:0.7.0')


Start a client
--------------

Start up an Ethereum client if you don't already have one running, such as `Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`_:

.. code-block:: bash

   $ geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

Or `Parity <https://github.com/ethcore/parity>`_:

.. code-block:: bash

   $ parity --chain testnet

If you don't want to run an Ethereum client, you can use `Infura <https://infura.io/>`_, which provides clients running in the cloud. For further information refer to the :doc:`infura` section.


Start sending requests
----------------------

To send asynchronous requests using a Future::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();


To send synchronous requests::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();


To use client admin commands::

   Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
   }



Further details
---------------
- web3j provides type safe access to all responses. Optional or null responses are wrapped in
  Java 8's
  `Optional <https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html>`_ type.
- Async requests are handled using Java 8's
  `CompletableFutures <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html>`_.
- Quantity payload types are returned as `BigIntegers <https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html>`_.
  For simple results, you can obtain the quantity as a String via
  `Response <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/Response.java>`_.getResult().
