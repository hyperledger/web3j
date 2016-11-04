Getting Started
===============

Add the latest web3j version to your project build configuration.

Maven
-----

.. code-block:: xml

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>1.0.0</version>
   </dependency>

Gradle
------

.. code-block:: groovy

   compile ('org.web3j:core:1.0.0')


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


Sending transactions
--------------------

web3j provides support for both working with Ethereum wallet files and Ethereum client admin
commands for sending transactions.

Using an Ethereum wallet file::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   // get the next available nonce
   EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();
   BigInteger nonce = ethGetTransactionCount.getTransactionCount();

   // create our transaction
   RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                nonce, <gas price>, <gas limit>, <toAddress>, <value>);

   // sign & sendn our transaction
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

Then generate the wrapper code:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Then you can then create and deploy a smart contract::::

   Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
   Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <credentials>, <initialEtherValue>,
           <param1>, ..., <paramN>).get();  // constructor params

Or use an existing::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>", <web3j>, <credentials>);

To transact with a smart contract::

   TransactionReceipt transactionReceipt = contract.someMethod(
                new Type(...),
                ...).get();

To call a smart contract::

   Type result = contract.someMethod(new Type(...), ...).get();

For more information refer to :ref:`smart-contract-wrappers`.


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
