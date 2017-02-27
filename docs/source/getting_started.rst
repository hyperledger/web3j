Getting Started
===============

Add the latest web3j version to your project build configuration.

Maven
-----

Java 8:

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>2.0.2</version>
   </dependency>

Android:

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core-android</artifactId>
     <version>2.0.0</version>
   </dependency>

Gradle
------

Java 8:

.. code-block:: groovy

   compile ('org.web3j:core:2.0.2')

Android:

.. code-block:: groovy

   compile ('org.web3j:core-android:2.0.0')


Start a client
--------------

Start up an Ethereum client if you don't already have one running, such as
`Geth <https://github.com/ethereum/go-ethereum/wiki/geth>`_:

.. code-block:: bash

   $ geth --fast --cache=512 --rpcapi personal,db,eth,net,web3 --rpc --testnet

Or `Parity <https://github.com/ethcore/parity>`_:

.. code-block:: bash

   $ parity --chain testnet

Or use `Infura <https://infura.io/>`_, which provides **free clients** running in the cloud:

.. code-block:: java

   Web3j web3 = Web3j.build(new InfuraHttpService("https://morden.infura.io/your-token"));

For further information refer to :doc:`infura`.


Start sending requests
----------------------

To send asynchronous requests using a Future::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();

To use an RxJava Observable::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   web3.web3ClientVersion().observable().subscribe(x -> {
       String clientVersion = x.getWeb3ClientVersion();
       ...
   });

To send synchronous requests::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
   String clientVersion = web3ClientVersion.getWeb3ClientVersion();

**Note:** for Android use:

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

**Note:** IPC is not currently available on web3j-android.


Filters
-------

web3j functional-reactive nature makes it really simple to setup observers that notify subscribers
of events taking place on the blockchain.

To receive all new blocks as they are added to the blockchain::

   Subscription subscription = web3j.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new transactions as they are added to the blockchain::

   Subscription subscription = web3j.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending transactions as they are submitted to the network (i.e. before they have
been grouped into a block together)::

   Subscription subscription = web3j.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Topic filters are also supported::

   EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                .addSingleTopic(...)|.addOptionalTopics(..., ...)|...;
   web3j.ethLogObservable(filter).subscribe(log -> {
       ...
   });

Subscriptions should always be cancelled when no longer required::

   subscription.unsubscribe();

**Note:** filters are not supported on Infura.

For further information refer to :doc:`filters`.


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
:ref:`smart contract wrappers <smart-contract-wrappers-summary>`.

Using an Ethereum client's admin commands (make sure you have your wallet in the client's
keystore)::

   Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
   }

.. _smart-contract-wrappers-summary:

Working with smart contracts with Java smart contract wrappers
--------------------------------------------------------------

web3j can auto-generate smart contract wrapper code to deploy and interact with smart contracts
without leaving Java.

To generate the wrapper code, compile your smart contract:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

Then generate the wrapper code using web3j's :doc:`command_line`:

.. code-block:: bash

   web3j solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

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

To transact with a smart contract::

   TransactionReceipt transactionReceipt = contract.someMethod(
                new Type(...),
                ...).get();

To call a smart contract::

   Type result = contract.someMethod(new Type(...), ...).get();

For more information refer to :ref:`smart-contract-wrappers`.


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
