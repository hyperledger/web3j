Application Binary Interface
============================

The Application Binary Interface (ABI) is a data encoding scheme used in Ethereum for working with
smart contracts. The types defined in the ABI are the same as those you encounter when writing
:doc:`smart_contracts` with Solidity - i.e. *uint8, ..., uint256, int8, ..., int256, bool, string,*
etc.

The `ABI module <https://github.com/web3j/web3j/tree/master/abi>`_ in web3j provides full support
for the ABI specification, and includes:

- Java implementations of all ABI types, including conversion from and to native Java types
- Function and event support
- Plenty of unit tests

Type mappings
-------------

The native Java to ABI type mappings used within web3j are as follows:

- boolean -> bool
- BigInteger -> uint/int
- byte[] -> bytes
- String -> string and address types
- List<> -> dynamic/static array

BigInteger types have to be used for numeric types, as numeric types in Ethereum are 256 bit
integer values.

`Fixed point types <http://solidity.readthedocs.io/en/develop/abi-spec.html#types>`_
have been defined for Ethereum, but are
`not currently implemented in Solidity <https://github.com/ethereum/solidity/issues/409>`_,
hence web3j does not currently support them (they were provided in versions prior to
3.x). Once available in Solidity, they will be reintroduced back into the web3j ABI module.

For more information on using ABI types in Java, refer to :ref:`smart-contract-wrappers`.

Further details
---------------

Please refer to the various
`ABI unit tests <https://github.com/web3j/web3j/tree/master/abi/src/test/java/org/web3j/abi>`_
for encoding/decoding examples.

A full ABI specification is maintained with the
`Solidity documentation <http://solidity.readthedocs.io/en/develop/abi-spec.html>`_.


Dependencies
------------

This is a very lightweight module, with the only third-party dependency being
`Bouncy Castle <https://www.bouncycastle.org/>`_ for cryptographic hashing
(`Spongy Castle <https://rtyley.github.io/spongycastle/>`_ on Android). The hope is that other
projects wishing to work with Ethereum's ABI on the JVM or Android will choose to make use of this
module rather then write their own implementations.
