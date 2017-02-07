.. To build this file locally ensure docutils Python package is installed and run:
   $ rst2html.py README.rst README.html

web3j: Web3 Java Ethereum Ðapp API
==================================

.. Will revert back to web3j badge (https://readthedocs.org/projects/web3j/badge/?version=latest),
   when they finally fix their build server issues for the project, see
   https://github.com/rtfd/readthedocs.org/issues/2476 for further information

.. image:: https://img.shields.io/badge/docs-latest-brightgreen.svg?style=flat
   :target: http://docs.web3j.io
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

web3j is a lightweight, reactive, type safe Java and Android library for integrating with clients (nodes) on
the Ethereum network:

.. image:: https://raw.githubusercontent.com/web3j/web3j/master/docs/source/images/web3j_network.png

This allows you to work with the `Ethereum <https://www.ethereum.org/>`_ blockchain, without the
additional overhead of having to write your own integration code for the platform.

The `Java and the Blockchain <https://www.youtube.com/watch?v=ea3miXs_P6Y>`_ talk provides an
overview of blockchain, Ethereum and web3j.


Features
--------

- Complete implementation of Ethereum's `JSON-RPC <https://github.com/ethereum/wiki/wiki/JSON-RPC>`_
  client API over HTTP and IPC
- Ethereum wallet support
- Auto-generation of Java smart contract wrappers to create, deploy, transact with and call smart
  contracts from native Java code
- Reactive-functional API for working with filters
- Support for Parity's
  `Personal <https://github.com/ethcore/parity/wiki/JSONRPC-personal-module>`__, and Geth's
  `Personal <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal>`__ client APIs
- Support for `Infura <https://infura.io/>`_, so you don't have to run an Ethereum client yourself
- Comprehensive integration tests demonstrating a number of the above scenarios
- Command line tools
- Android compatible
- Support for JP Morgan's Quorum via `web3j-quorum <https://github.com/web3j/quorum>`_


It has seven runtime dependencies:

- `RxJava <https://github.com/ReactiveX/RxJava>`_ for its reactive-functional API
- `Apache HTTP Client <https://hc.apache.org/httpcomponents-client-ga/index.html>`_
- `Jackson Core <https://github.com/FasterXML/jackson-core>`_ for fast JSON
  serialisation/deserialisation
- `Bouncy Castle <https://www.bouncycastle.org/>`_ and
  `Java Scrypt <https://github.com/wg/scrypt>`_ for crypto
- `JavaPoet <https://github.com/square/javapoet>`_ for generating smart contract wrappers
- `Jnr-unixsocket <https://github.com/jnr/jnr-unixsocket>`_ for \*nix IPC

Full project documentation is available at
`Read the Docs <http://docs.web3j.io>`_.


Getting started
---------------

Add the relevant dependency to your project:

Maven
-----

Java 8:

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>1.1.2</version>
   </dependency>

Android:

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core-android</artifactId>
     <version>1.1.0</version>
   </dependency>

Gradle
------

Java 8:

.. code-block:: groovy

   compile ('org.web3j:core:1.1.2')

Android:

.. code-block:: groovy

   compile ('org.web3j:core-android:1.1.0')


Start a client
--------------

Start up an Ethereum client if you don't already have one running, such as
`Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`_:

.. code-block:: bash

   $ geth --rpcapi personal,db,eth,net,web3 --rpc --testnet

Or `Parity <https://github.com/ethcore/parity>`_:

.. code-block:: bash

   $ parity --chain testnet


Or use `Infura <https://infura.io/>`_, which provides **free clients** running in the cloud:

.. code-block:: java

   Web3j web3 = Web3j.build(new InfuraHttpService("https://morden.infura.io/your-token"));

For further information refer to
`Using Infura with web3j <https://web3j.github.io/web3j/infura.html>`_


Start sending requests
----------------------

To send asynchronous requests using a Future:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();

To use an RxJava Observable:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   web3.web3ClientVersion().observable().subscribe(x -> {
       String clientVersion = x.getWeb3ClientVersion();
       ...
   });

To send synchronous requests:

.. code-block:: java

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();

**Note:** for Android use:

.. code-block:: java

   Web3j web3 = Web3jFactory.build(new HttpService());  // defaults to http://localhost:8545/
   ...


IPC
---

web3j also supports fast inter-process communication (IPC) via file sockets to clients running on
the same host as web3j. To connect simply use the relevent *IpcService* implemntation instead of
*HttpService* when you create your service:

.. code-block:: java

   // OS X/Linux/Unix:
   Web3j web3 = Web3j.build(new UnixIpcService("/path/to/socketfile"));
   ...

   // Windows
   Web3j web3 = Web3j.build(new WindowsIpcService("/path/to/namedpipefile"));
   ...



Filters
-------

web3j functional-reactive nature makes it really simple to setup observers that notify subscribers
of events taking place on the blockchain.

To receive all new blocks as they are added to the blockchain:

.. code-block:: java

   Subscription subscription = web3j.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new transactions as they are added to the blockchain:

.. code-block:: java

   Subscription subscription = web3j.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending transactions as they are submitted to the network (i.e. before they have
been grouped into a block together):

.. code-block:: java

   Subscription subscription = web3j.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Topic filters are also supported:

.. code-block:: java

   EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                .addSingleTopic(...)|.addOptionalTopics(..., ...)|...;
   web3j.ethLogObservable(filter).subscribe(log -> {
       ...
   });

Subscriptions should always be cancelled when no longer required:

.. code-block:: java

   subscription.unsubscribe();

**Note:** filters are not supported on Infura.

For further information refer to `Filters and Events <http://docs.web3j.io/filters.html>`_.


Transactions
------------

web3j provides support for both working with Ethereum wallet files (recommended) and Ethereum
client admin commands for sending transactions.

To send Ether to another party using your Ethereum wallet file::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
   TransactionReceipt transactionReceipt = Transfer.sendFunds(
           web3, credentials, "0x...", BigDecimal.valueOf(1.0), Convert.Unit.ETHER);

Or if you wish to create your own custom transaction::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   // get the next available nonce
   EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();
   BigInteger nonce = ethGetTransactionCount.getTransactionCount();

   // create our transaction
   RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce, <gas price>, <gas limit>, <toAddress>, <value>);

   // sign & send our transaction
   byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
   String hexValue = Hex.toHexString(signedMessage);
   EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
   // ...

Although it's far simpler using web3j's
`Java smart contract wrappers`_.

Using an Ethereum client's admin commands (make sure you have your wallet in the client's
keystore)::

   Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
   }


Java smart contract wrappers
----------------------------

web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts
without leaving Java.

To generate the wrapper code, compile your smart contract:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

Then generate the wrapper code using web3j's `Command line tools`_:

.. code-block:: bash

   web3j solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Or in code:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name


Now you can create and deploy your smart contract::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <credentials>,
           GAS_PRICE, GAS_LIMIT,
           <initialEtherValue>,
           <param1>, ..., <paramN>).get();  // constructor params

Or use an existing::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>", <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT);

To Transact with a smart contract::

   TransactionReceipt transactionReceipt = contract.someMethod(
                new Type(...),
                ...).get();

To call a smart contract::

   Type result = contract.someMethod(new Type(...), ...).get();

For more information refer to the `documentation <http://docs.web3j.io/>`_.


Command line tools
------------------

A web3j fat jar is distributed with each release providing command line tools. The command line
tools allow you to use some of the functionality of web3j from the command line:

- Wallet creation
- Wallet password management
- Transfer of funds from one wallet to another
- Generate Solidity smart contract function wrappers

Please refer to the `documentation <http://docs.web3j.io/command_line.html>`_ for further
information.


Further details
---------------

In the Java 8 build:

- web3j provides type safe access to all responses. Optional or null responses
  are wrapped in Java 8's
  `Optional <https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html>`_ type.
- Async requests are handled using Java 8's
  `CompletableFutures <https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html>`_.

In both the Java 8 and Andriod builds:

- Quantity payload types are returned as `BigIntegers <https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html>`_.
  For simple results, you can obtain the quantity as a String via
  `Response <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/Response.java>`_.getResult().


Tested clients
--------------

- Geth
- Parity

You can run the integration test class
`CoreIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/core/CoreIT.java>`_
to verify clients.


Related projects
----------------

For a .NET implementation, check out `Nethereum <https://github.com/Nethereum/Nethereum>`_.

For a pure Java implementation of the Ethereum client, check out
`EthereumJ <https://github.com/ethereum/ethereumj>`_ and the work of
`Ether.Camp <https://github.com/ether-camp/>`_.


Projects using web3j
--------------------

- `Ether Wallet <https://play.google.com/store/apps/details?id=org.vikulin.etherwallet>`_ by
  `@vikulin <https://github.com/vikulin>`_
- `eth-contract-api <https://github.com/adridadou/eth-contract-api>`_ by
  `@adridadou <https://github.com/adridadou>`_
- `Ethereum Paper Wallet <https://github.com/matthiaszimmermann/ethereum-paper-wallet>`_ by
  `@matthiaszimmermann <https://github.com/matthiaszimmermann>`_


Build instructions
------------------

web3j includes integration tests for running against a live Ethereum client. If you do not have a
client running, you can exclude their execution as per the below instructions.

To run a full build including integration tests:

.. code-block:: bash

   $ ./gradlew check


To run excluding integration tests:

.. code-block:: bash

   $ ./gradlew -x integrationTest check

Thanks and credits
------------------

- The `Nethereum <https://github.com/Nethereum/Nethereum>`_ project for the inspiration
- `Othera <https://www.othera.com.au/>`_ for the great things they are building on the platform
- `Finhaus <http://finhaus.com.au/>`_ guys for putting me onto Nethereum
- `bitcoinj <https://bitcoinj.github.io/>`_ for the reference Elliptic Curve crypto implementation
- Everyone involved in the Ethererum project and its surrounding ecosystem
- And of course the users of the library, who've provided valuable input & feedback -
  `@ice09 <https://github.com/ice09>`_, `@adridadou <https://github.com/adridadou>`_,
  `@nickmelis <https://github.com/nickmelis>`_, `@basavk <https://github.com/basavk>`_,
  `@kabl <https://github.com/kabl>`_, `@MaxBinnewies <https://github.com/MaxBinnewies>`_,
  `@vikulin <https://github.com/vikulin>`_, `@sullis <https://github.com/sullis>`_,
  `@vethan <https://github.com/vethan>`_, `@h2mch <https://github.com/h2mch>`_,
  `@mtiutin <https://github.com/mtiutin>`_, `@fooock <https://github.com/fooock>`_,
  `@ermyas <https://github.com/ermyas>`_, `@danieldietrich <https://github.com/danieldietrich>`_,
  `@matthiaszimmermann <https://github.com/matthiaszimmermann>`_,
  `@ferOnti <https://github.com/ferOnti>`_, `@fraspadafora <https://github.com/fraspadafora>`_,
  `@bigstar119 <https://github.com/bigstar119>`_, `@gagarin55 <https://github.com/gagarin55>`_,
  `@thedoctor <https://github.com/thedoctor>`_
