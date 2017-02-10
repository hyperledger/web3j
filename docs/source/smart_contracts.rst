Smart Contracts
===============

Developers have the choice of three languages for writing smart contracts:

`Solidity <https://solidity.readthedocs.io/>`_
  The flagship language of Ethereum, and most popular language for smart contracts.

`Serpent <https://github.com/ethereum/wiki/wiki/Serpent>`_
  A Python like language for writing smart contracts.

LISP Like Language (LLL)
  A low level language, Serpent provides a superset of LLL. There's not a great deal of information
  for working with LLL, the following blog `/var/log/syrinx <http://blog.syrinx.net/>`_ and
  associated `GitHub <https://github.com/zigguratt/lll-resurrected>`_ is a good place to start.


In order to deploy a smart contract onto the Ethereum blockchain, it must first be compiled into
a bytecode format, then it can be sent as part of a transaction request as detailed in
:ref:`creation-of-smart-contract`.

Given that Solidity is the language of choice for writing smart contracts, it is the language
supported by web3j, and is used for all subsequent examples.


Getting started with Solidity
-----------------------------

An overview of Solidity is beyond the scope of these docs, however, the following resources are a
good place to start:

- `Contract Tutorial <https://github.com/ethereum/go-ethereum/wiki/Contract-Tutorial>`_ on the Go
  Ethereum Wiki
- `Introduction to Smart Contracts <http://solidity.readthedocs.io/en/develop/introduction-to-smart-contracts.html>`_
  in the Solidity project documentation
- `Writing a contract <https://ethereum-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#writing-a-contract>`_
  in the Ethereum Homestead Guide

.. _compiling-solidity:

Compiling Solidity source code
------------------------------

Compilation to bytecode is performed by the Solidity compiler, *solc*. You can install the compiler,
locally following the instructions as per
`the project documentation <https://solidity.readthedocs.io/en/latest/installing-solidity.html>`_.

To compile the solidity code run:

.. code-block:: bash

   $ solc <contract>.sol --bin --abi --optimize -o <output-dir>/

**Note**: there are issues with installing solc via homebrew on OS X currently, please see the
following `thread <https://github.com/ethereum/cpp-ethereum/issues/3173#issuecomment-255991056>`_
for further information.

The *--bin* and *--abi* compiler arguments are both required to take full advantage of working
with smart contracts from web3j.

*--bin*
  Outputs a Solidity binary file containing the hex-encoded binary to provide with the transaction
  request (as per :ref:`creation-of-smart-contract`).

*--abi*
  Outputs a solidity application binary interface (ABI) file which details all of the publicly
  acessible contract methods and their associated parameters. These details along with the
  contract address are crucial for interacting with smart contracts. The ABI file is also used for
  the generation of :ref:`smart-contract-wrappers`.

There is also a *--gas* argument for providing estimates of the :ref:`gas` required to create a
contract and transact with its methods.


Alternatively, you can write and compile solidity code in your browser via the
`Browser-Solidity <https://ethereum.github.io/browser-solidity/>`_. Browser-Solidity is great for
smaller smart contracts, but you may run into issues working with larger contracts.

You can also compile solidity code via Ethereum clients such as Geth and Parity, using the JSON-RPC
method `eth_compileSolidity <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compilesolidity>`_
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
`src/test/resources/solidity <https://github.com/web3j/web3j/tree/master/src/test/resources/solidity>`_

It also provides integration tests for demonstrating the deploying and working with those smart
contracts in the project directory
`src/integration-test/java/org/web3j/protocol/scenarios <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/protocol/scenarios>`_.


.. image:: /images/smart_contract.png

.. _eip:

EIP-20 Ethereum token standard smart contract
---------------------------------------------

There is an active `Ethereum Improvement Proposal (EIP) <https://github.com/ethereum/EIPs>`_,
`EIP-20 <https://github.com/ethereum/EIPs/issues/20>`_ that has been created to define the standard
functions that a smart contract providing tokens can implement.

The EIP-20 proposal provides function definitions, but does not provide an implementation example.
However, there is an implementation provided in
`src/test/resources/solidity/contracts <https://github.com/web3j/web3j/tree/master/src/test/resources/solidity/contracts>`_,
which has been taken from Consensys'
`Tokens project <https://github.com/ConsenSys/Tokens>`_.

There are two integration tests that have been written to fully demonstrate the functionality of
this token smart contract.

`HumanStandardTokenGeneratedIT <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/protocol/scenarios/HumanStandardTokenGeneratedIT.java>`_
uses the generated
`HumanStandardTokenGenerated <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/generated/HumanStandardTokenGenerated.java>`_
:ref:`smart contract wrapper <smart-contract-wrappers>` to demonstrate this.

Alternatively, if you do not wish to use a smart contract wrapper and would like to work directly
with the JSON-RPC calls, please refer to
`HumanStandardTokenIT <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/protocol/scenarios/HumanStandardTokenIT.java>`_.


.. _smart-contract-wrappers:

Solidity smart contract wrappers
--------------------------------

web3j supports the auto-generation of smart contract function wrappers in Java from Solidity ABI
files.

The web3j :doc:`command_line` tools ship with a command line utility for generating the smart contract function wrappers:

.. code-block:: bash

   $ web3j solidity generate /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Or by calling the Java class directly:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name

Where the *bin* and *abi* are obtained as per :ref:`compiling-solidity`.

The smart contract wrappers support all common operations for working with smart contracts:

- :ref:`construction-and-deployment`
- :ref:`invoking-transactions`
- :ref:`constant-methods`

Any method calls that requires an underlying JSON-RPC call to take place will return a Future to
avoid blocking.


.. _construction-and-deployment:

Construction and deployment
---------------------------

Construction and deployment of smart contracts happens with the *deploy* method::

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT,
           <initialValue>,
           <param1>, ..., <paramN>);

This will create a new instance of the smart contract on the Ethereum blockchain using the
supplied credentials, and constructor parameter values.

It returns a new smart contract wrapper instance which contains the underlying address of the
smart contract. If you wish to construct an instance of a smart contract wrapper with an existing
smart contract, simply pass in it's address::

   YourSmartContract contract = new YourSmartContract(
           "0x...", web3j, credentials, GAS_PRICE, GAS_LIMIT);


.. _transaction-managers:

Transaction Managers
--------------------

web3j provides a
`TransactionManager <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/tx/TransactionManager.java>`_
abstraction to control the manner you connect to Ethereum clients with. The default mechanism uses
web3j's
`RawTransactionManager <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/tx/RawTransactionManager.java>`_
which works with Ethereum wallet files to sign transactions offline before submitting them to the
network. However, you may wish to modify the transaction manager, which you can pass to the smart
contract deployment and creation methods instead of a credentials object, i.e.::

   YourSmartContract contract = YourSmartContract.deploy(
           <web3j>, <transactionManager>, GAS_PRICE, GAS_LIMIT,
           <initialValue>,
           <param1>, ..., <paramN>);

In addition to the RawTransactionManager, web3j provides a
`ClientTransactionManager <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/tx/ClientTransactionManager.java>`_
which passes the responsibility of signing your transaction on to the Ethereum client you are
connecting to.


Specifying the Chain Id on Transactions (EIP-155)
-------------------------------------------------

The RawTransactionManager takes an optional *chainId* parameter to specify the chain id to be used
on transactions as per
`EIP-155 <https://github.com/ethereum/EIPs/issues/155>`_. This prevents transactions from one chain
being re-broadcast onto another chain, such as from Morden to Mainnet::

   TransactionManager transactionManager = new RawTransactionManager(
           web3j, credentials, ChainId.MAIN_NET);

In order to avoid having to change config or code to specify which chain you are working with,
web3j's default behaviour is to not specify chain ids on transactions to simplify working with the
library. However, the recommendation of the Ethereum community is to use them.


.. _invoking-transactions:

Invoking transactions and events
--------------------------------

All transactional smart contract methods are named identically to their Solidity methods, taking
the same parameter values. Transactional calls do not return any values, regardless of the return
type specified on the method. Hence, for all transactional methods the
`Transaction Receipt <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt>`_
associated with the transaction is returned.::

   TransactionReceipt transactionReceipt = contract.someMethod(
                new Type(...),
                ...).get();


The transaction receipt is useful for two reasons:

#. It provides details of the mined block that the transaction resides in
#. `Solidity events <http://solidity.readthedocs.io/en/develop/contracts.html?highlight=events#events>`_ that
   are called will be logged as part of the transaction, which can then be extracted

Any events defined within a smart contract will be represented in the smart contract wrapper with
a method named *process<Event Name>Event*, which takes the Transaction Receipt and from this
extracts the indexed and non-indexed event parameters, which are returned decoded in an instance of
the
`EventValues <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/abi/EventValues.java>`_
object.::

   EventValues eventValues = contract.processSomeEvent(transactionReceipt);

**Remember** that for any indexed array, bytes and string Solidity parameter
types, a Keccak-256 hash of their values will be returned, see the
`documentation <http://solidity.readthedocs.io/en/latest/contracts.html#events>`_
for further information.


.. _constant-methods:

Calling constant methods
------------------------

Constant methods are those that read a value in a smart contract, and do not alter the state of
the smart contract. These methods are available with the same method signature as the smart
contract they were generated from, the only addition is that the call is wrapped in a Future.::

   Type result = contract.someMethod(new Type(...), ...).get();


Examples
--------

Please refer to :ref:`eip`.
