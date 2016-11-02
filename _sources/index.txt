.. web3j documentation master file

web3j
=====

web3j is a lightweight, type safe Java library for integrating with clients (nodes) on the Ethereum network:

.. code-block:: bash

   [ JVM application ] + [ web3j ] <---> [ Ethereum node ]

This allows you to work with the `Ethereum <https://www.ethereum.org/>`_ blockchain, without the additional overhead of having to write your own integration code for the platform.


Features
========

- Full support for the Ethereum `JSON-RPC <https://github.com/ethereum/wiki/wiki/JSON-RPC>`_ specification
- Offline transaciton signing
- Generation of Java smart contract wrappers to interact with a smart contract via native Java code.
- Support for Parity's `Personal <https://github.com/ethcore/parity/wiki/JSONRPC-personal-module>`__, and Geth's `Personal <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal>`__ APIs
- Support for `Infura <https://infura.io/>`_, so you don't have to run an Ethereum client yourself
- Comprehensive integration tests demonstrating a number of the above scenarios


Dependencies
============

It only has four runtime dependencies:

- `Apache HTTP Client <https://hc.apache.org/httpcomponents-client-ga/index.html>`_
- `Jackson Core <https://github.com/FasterXML/jackson-core>`_ for fast JSON serialisation/deserialisation
- `Bouncy Castle <https://www.bouncycastle.org/>`_ for crypto
- `JavaPoet <https://github.com/square/javapoet>`_ for generating smart contract wrappers


Contents:
=========

.. toctree::
   :maxdepth: 2

   introduction.rst
   getting_started.rst
   transactions.rst
   smart_contracts.rst
   filters.rst
   callbacks.rst
   rlp.rst
   abi.rst
   management_apis.rst
   infura.rst
   trouble.rst
   development.rst
   links.rst
   credits.rst
