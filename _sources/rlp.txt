Recursive Length Prefix
=======================

The Recursive Length Prefix (RLP) encoding scheme is a space efficient object serialization scheme
used in Ethereum.

The specification itself is defined in the `Yellow Paper <http://gavwood.com/paper.pdf>`_,
and the following page on the `Ethereum Wiki <https://github.com/ethereum/wiki/wiki/RLP>`_.


RLP Types
---------

The RLP encoder defined two supported types:

- string
- list

The list type can be nested an arbitrary number of times allowing complex data structures to be
encoded.


The `RLP module <https://github.com/web3j/web3j/tree/master/rlp>`_ in web3j provides RLP encoding
capabilities, with the
`RlpEncoderTest <https://github.com/web3j/web3j/blob/master/rlp/src/test/java/org/web3j/rlp/RlpEncoderTest.java>`_
demonstrating encoding of a number of different values.


Transaction encoding
--------------------

Within web3j, RLP encoding is used to encode Ethereum transaction objects into a byte array which
is signed before submission to the network. The transaction types and signing logic are located
within the Crypto module, with the
`TransactionEncoderTest <https://github.com/web3j/web3j/blob/master/crypto/src/test/java/org/web3j/crypto/TransactionEncoderTest.java>`_
providing examples of transaction signing and encoding.


Dependencies
------------

This is a very lightweight module, with no other dependencies. The hope is that other
projects wishing to work with Ethereum's RLP encoding on the JVM or Android  will choose to make
use of this module rather then write their own implementations.
