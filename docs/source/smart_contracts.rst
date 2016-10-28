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
  associated `Github <https://github.com/zigguratt/lll-resurrected>`_ is a good place to start.


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
-------------------------------------

Please refer to the sections :ref:`creation-of-smart-contract`, :ref:`transacting-with-contract`
and :ref:`querying-state` for details.


Smart contract examples
-----------------------

web3j provides a number of smart contract examples in the project directory
`src/test/resources/solidity <https://github.com/web3j/web3j/tree/master/src/test/resources/solidity>`_

It also provides integration tests for demonstrating the deploying and working with those smart
contracts in the project directory
`src/integration-test/java/org/web3j/protocol/scenarios <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/protocol/scenarios>`_.


EIP-20 Ethereum token standard smart contract
---------------------------------------------

There is an active `Ethereum Improvement Proposal (EIP) <https://github.com/ethereum/EIPs>`_,
`EIP-20 <https://github.com/ethereum/EIPs/issues/20>`_ that has been created to define the standard
functions that a smart contract providing tokens can implement.

The EIP-20 proposal provides function definitions, but does not provide an implementation example.
However, there is an implementation provided in
`src/test/resources/solidity/contracts <https://github.com/web3j/web3j/tree/master/src/test/resources/solidity/contracts>`_,
which has been taken from Consensys' implementation on
`GitHub <https://github.com/ConsenSys/Tokens>`_.

The integration test
`HumanStandardTokenIT <https://github.com/web3j/web3j/tree/master/src/integration-test/java/org/web3j/protocol/scenarios/HumanStandardTokenIT.java>`_.
has been been written to fully demonstrate the functionality of this token standard smart contract.


.. _smart-contract-wrappers:

Solidity smart contract wrappers
--------------------------------

web3j supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

This can be achieved by running:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.abi -o /path/to/src/dir/java -p com.your.organisation.name

See `FunctionWrappersIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/FunctionWrappersIT.java>`_
for an example of using a generated smart contract Java wrapper.

**Note:** at present the wrappers invoke smart contracts via
`EthCall <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call>`_,
so a transaction does not take place. Transaction support is imminent.

For an example of how to call a smart contracts via a transaction, refer to
`DeployContractIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/DeployContractIT.java>`_.
