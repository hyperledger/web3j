Smart Contracts
===============

Developers have the choice of three languages for writing smart contracts:

`Solidity <https://Solidity.readthedocs.io/>`_
  The flagship language of Ethereum, and most popular language for smart contracts.

`Serpent <https://github.com/ethereum/wiki/wiki/Serpent>`_
  A Python like language for writing smart contracts.

LISP Like Language (LLL)
  A low level language, Serpent provides a superset of LLL. There's not a great deal of information
  for working with LLL, the following blog `/var/log/syrinx <http://blog.syrinx.net/>`_ and
  associated `GitHub <https://github.com/zigguratt/lll-resurrected>`_ is a good place to start.


In order to deploy a smart contract onto the Ethereum blockchain, it must first be compiled into
a bytecode format, then it can be sent as part of a transaction. web3j can do all of this for you
 with its :ref:`smart-contract-wrappers`. To understand what is happening behind the scenes, you
 can refer to the details in :ref:`creation-of-smart-contract`.

Given that Solidity is the language of choice for writing smart contracts, it is the language
supported by web3j, and is used for all subsequent examples.


Getting started with Solidity
-----------------------------

An overview of Solidity is beyond the scope of these docs, however, the following resources are a
good place to start:

- `Contract Tutorial <https://github.com/ethereum/go-ethereum/wiki/Contract-Tutorial>`_ on the Go
  Ethereum Wiki
- `Introduction to Smart Contracts <http://Solidity.readthedocs.io/en/develop/introduction-to-smart-contracts.html>`_
  in the Solidity project documentation
- `Writing a contract <https://ethereum-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#writing-a-contract>`_
  in the Ethereum Homestead Guide

.. _compiling-Solidity:

Compiling Solidity source code
------------------------------

Compilation to bytecode is performed by the Solidity compiler, *solc*. You can install the compiler,
locally following the instructions as per
`the project documentation <http://solidity.readthedocs.io/en/develop/installing-solidity.html>`_.

To compile the Solidity code run:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

The *--bin* and *--abi* compiler arguments are both required to take full advantage of working
with smart contracts from web3j.

*--bin*
  Outputs a Solidity binary file containing the hex-encoded binary to provide with the transaction
  request.

*--abi*
  Outputs a Solidity application binary interface (ABI) file which details all of the publicly
  accessible contract methods and their associated parameters. These details along with the
  contract address are crucial for interacting with smart contracts. The ABI file is also used for
  the generation of :ref:`smart-contract-wrappers`.

There is also a *--gas* argument for providing estimates of the :ref:`gas` required to create a
contract and transact with its methods.


Alternatively, you can write and compile Solidity code in your browser via the
`browser-solidity <https://ethereum.github.io/browser-solidity/>`_ project. browser-solidity is
great for smaller smart contracts, but you may run into issues working with larger contracts.

You can also compile Solidity code via Ethereum clients such as Geth and Parity, using the JSON-RPC
method `eth_compileSolidity <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compileSolidity>`_
which is also supported in web3j. However, the Solidity compiler must be installed on the client
for this to work.

There are further options available, please refer to the
`relevant section <https://ethereum-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#compiling-a-contract>`_
in the Homestead documentation.


Deploying and interacting with smart contracts
----------------------------------------------

If you want to avoid the underlying implementation detail for working with smart contracts, web3j
provides :ref:`smart-contract-wrappers` which enable you to interact directly with all of a smart
contract's methods via a generated wrapper object.

Alternatively, if you wish to send regular transactions or have more control over your
interactions with your smart contracts, please refer to the sections
:ref:`creation-of-smart-contract`, :ref:`transacting-with-contract` and :ref:`querying-state`
for details.


Smart contract examples
-----------------------

web3j provides a number of smart contract examples in the project directory
`codegen/src/test/resources/solidity <https://github.com/web3j/web3j/tree/master/codegen/src/test/resources/solidity>`_

It also provides integration tests for demonstrating the deploying and working with those smart
contracts in the
`integration-tests/src/test/java/org/web3j/protocol/scenarios <https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios>`_
module.

.. image:: /images/smart_contract.png

.. _eip:

EIP-20 Ethereum token standard smart contract
---------------------------------------------

There an Ethereum standard, `EIP-20 <https://github.com/ethereum/EIPs/issues/20>`_
which started off as an
`Ethereum Improvement Proposal (EIP) <https://github.com/ethereum/EIPs>`_, that defines the
standard functions that a smart contract providing tokens should implement.

The EIP-20 standard provides function definitions, but does not provide an implementation example.
However, there is an implementation provided in
`codegen/src/test/resources/solidity/contracts <https://github.com/web3j/web3j/tree/master/codegen/src/test/resources/solidity/contracts>`_,
which has been taken from ConsenSys'
`Tokens project <https://github.com/ConsenSys/Tokens>`_.

Open Zepplin also provide an example implementation on
`GitHub <https://github.com/OpenZeppelin/zeppelin-solidity/tree/master/contracts/token>`_.

There are two integration tests that have been written to fully demonstrate the functionality of
this token smart contract.

`HumanStandardTokenGeneratedIT <https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/HumanStandardTokenGeneratedIT.java>`_
uses the generated
`HumanStandardTokenGenerated <https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/generated/HumanStandardTokenGenerated.java>`_
:ref:`smart contract wrapper <smart-contract-wrappers>` to demonstrate this.

Alternatively, if you do not wish to use a smart contract wrapper and would like to work directly
with the underlying JSON-RPC calls, please refer to
`HumanStandardTokenIT <https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/HumanStandardTokenIT.java>`_.


.. _smart-contract-wrappers:

Solidity smart contract wrappers
--------------------------------

web3j supports the auto-generation of smart contract function wrappers in Java from Solidity ABI
files.

The web3j :doc:`command_line` tools ship with a command line utility for generating the smart contract function wrappers:

.. code-block:: bash

   $ web3j solidity generate [--javaTypes|--solidityTypes] /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

In versions prior to 3.x of web3j, the generated smart contract wrappers used native Solidity
types. From web3j 3.x onwards, Java types are created by default. You can create Solidity types
using the *--solidityTypes* command line argument.

You can also generate the wrappers by calling then Java class directly:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Where the *bin* and *abi* are obtained as per :ref:`compiling-Solidity`.

The smart contract wrappers support all common operations for working with smart contracts:

- :ref:`construction-and-deployment`
- :ref:`invoking-transactions`
- :ref:`constant-methods`
- :ref:`contract-validity`

Any method calls that requires an underlying JSON-RPC call to take place will return a Future to
avoid blocking.


.. _construction-and-deployment:

Construction and deployment
---------------------------

Construction and deployment of smart contracts happens with the *deploy* method::

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT,
           [<initialValue>,]
           <param1>, ..., <paramN>).send();

This will create a new instance of the smart contract on the Ethereum blockchain using the
supplied credentials, and constructor parameter values.

The *<initialValue>* parameter is only required if your smart contract accepts Ether on
construction. This requires the Solidity
`payable <http://solidity.readthedocs.io/en/develop/frequently-asked-questions.html?highlight=payable#how-do-i-initialize-a-contract-with-only-a-specific-amount-of-wei>`_
modifier to be present in the contract.

It returns a new smart contract wrapper instance which contains the underlying address of the
smart contract. If you wish to construct an instance of a smart contract wrapper with an existing
smart contract, simply pass in it's address::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>", web3j, credentials, GAS_PRICE, GAS_LIMIT);

.. _contract-validity:

Using this method, you may want to ascertain that the contract address that you have loaded is the
smart contract that you expect. For this you can use the *isValid* smart contract method, which will
only return true if the deployed bytecode at the contract address matches the bytecode in the
smart contract wrapper.::

   contract.isValid();  // returns false if the contract bytecode does not match what's deployed
                        // at the provided addres


.. _transaction-managers:

Transaction Managers
--------------------

web3j provides a
`TransactionManager <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/TransactionManager.java>`_
abstraction to control the manner you connect to Ethereum clients with. The default mechanism uses
web3j's
`RawTransactionManager <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/RawTransactionManager.java>`_
which works with Ethereum wallet files to sign transactions offline before submitting them to the
network.

However, you may wish to modify the transaction manager, which you can pass to the smart
contract deployment and creation methods instead of a credentials object, i.e.::

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <transactionManager>, GAS_PRICE, GAS_LIMIT,
           <param1>, ..., <paramN>).send();

In addition to the RawTransactionManager, web3j provides a
`ClientTransactionManager <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/tx/ClientTransactionManager.java>`_
which passes the responsibility of signing your transaction on to the Ethereum client you are
connecting to.


Specifying the Chain Id on Transactions (EIP-155)
-------------------------------------------------

The RawTransactionManager takes an optional *chainId* parameter to specify the chain id to be used
on transactions as per
`EIP-155 <https://github.com/ethereum/EIPs/issues/155>`_. This prevents transactions from one chain
being re-broadcast onto another chain, such as from Ropsten to Mainnet::

   TransactionManager transactionManager = new RawTransactionManager(
           web3j, credentials, ChainId.MAIN_NET);

In order to avoid having to change config or code to specify which chain you are working with,
web3j's default behaviour is to not specify chain ids on transactions to simplify working with the
library. However, the recommendation of the Ethereum community is to use them.

You can obtain the chain id of the network that your Ethereum client is connected to with the
following request::

   web3j.netVersion().send().getNetVersion();


.. transaction-processors:

Transaction Receipt Processors
------------------------------

By default, when a new transaction is submitted by web3j to an Ethereum client, web3j will
continually poll the client until it receives a
`TransactionReceipt <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/protocol/core/methods/response/TransactionReceipt.java>`_,
indicating that the transaction has been added to the blockchain. If you are sending a number of
transactions asynchronously with web3j, this can result in a number of threads polling the client
concurrently.

To reduce this polling overhead, web3j provides configurable
`TransactionReceiptProcessors <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/TransactionReceiptProcessor.java>`_.

There are a number of processors provided in web3j:

- `PollingTransactionReceiptProcessor <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/PollingTransactionReceiptProcessor.java>`_
  is the default processor used in web3j, which polls periodically for a transaction receipt for
  each individual pending transaction.
- `QueuingTransactionReceiptProcessor <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/QueuingTransactionReceiptProcessor.java>`_
  has an internal queue of all pending transactions. It contains a worker that runs periodically
  to query if a transaction receipt is available yet. If a receipt is found, a callback to the
  client is invoked.
- `NoOpProcessor <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/NoOpProcessor.java>`_
  provides an
  `EmptyTransactionReceipt <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/EmptyTransactionReceipt.java>`_
  to clients which only contains the transaction hash. This is for clients who do not want web3j
  to perform any polling for a transaction receipt.

**Note:** the
`EmptyTransactionReceipt <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/EmptyTransactionReceipt.java>`_
is also provided in the the initial response from the `QueuingTransactionReceiptProcessor <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/QueuingTransactionReceiptProcessor.java>`_.
This allows the caller to have the transaction hash for the transaction that was submitted to the
network.

If you do not wish to use the default processor
(`PollingTransactionReceiptProcessor <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/PollingTransactionReceiptProcessor.java>`_), you can
specify the transaction receipt processor to use as follows::

   TransactionReceiptProcessor transactionReceiptProcessor =
           new QueuingTransactionReceiptProcessor(web3j, new Callback() {
                    @Override
                    public void accept(TransactionReceipt transactionReceipt) {
                        // process transactionReceipt
                    }

                    @Override
                    public void exception(Exception exception) {
                        // handle exception
                    }
   TransactionManager transactionManager = new RawTransactionManager(
           web3j, credentials, ChainId.MAIN_NET, transactionReceiptProcessor);


If you require further information, the
`FastRawTransactionManagerIT <https://github.com/web3j/web3j/blob/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/FastRawTransactionManagerIT.java>`_
demonstrates the polling and queuing approaches.


.. _invoking-transactions:

Invoking transactions and events
--------------------------------

All transactional smart contract methods are named identically to their Solidity methods, taking
the same parameter values. Transactional calls do not return any values, regardless of the return
type specified on the method. Hence, for all transactional methods the
`Transaction Receipt <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt>`_
associated with the transaction is returned.::

   TransactionReceipt transactionReceipt = contract.someMethod(
                <param1>,
                ...).send();


The transaction receipt is useful for two reasons:

#. It provides details of the mined block that the transaction resides in
#. `Solidity events <http://Solidity.readthedocs.io/en/develop/contracts.html?highlight=events#events>`_
   that are called will be logged as part of the transaction, which can then be extracted

Any events defined within a smart contract will be represented in the smart contract wrapper with
a method named *process<Event Name>Event*, which takes the Transaction Receipt and from this
extracts the indexed and non-indexed event parameters, which are returned decoded in an instance of
the
`EventValues <https://github.com/web3j/web3j/blob/master/abi/src/main/java/org/web3j/abi/EventValues.java>`_
object.::

   EventValues eventValues = contract.processSomeEvent(transactionReceipt);

Alternatively you can use an Observable filter instead which will listen for events associated with
the smart contract::

   contract.someEventObservable(startBlock, endBlock).
           .subscribe(event -> ...);

For more information on working with Observable filters, refer to :doc:`filters`.

**Remember** that for any indexed array, bytes and string Solidity parameter
types, a Keccak-256 hash of their values will be returned, see the
`documentation <http://Solidity.readthedocs.io/en/latest/contracts.html#events>`_
for further information.


.. _constant-methods:

Calling constant methods
------------------------

Constant methods are those that read a value in a smart contract, and do not alter the state of
the smart contract. These methods are available with the same method signature as the smart
contract they were generated from::

   Type result = contract.someMethod(<param1>, ...).send();







Examples
--------

Please refer to :ref:`eip`.
