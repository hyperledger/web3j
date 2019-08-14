Modules
=======

To provide greater flexibility for developers wishing to work with web3j, the project is made up of a number of modules.

In dependency order, they are as follows:

-   utils - Minimal set of utility classes
-   rlp - Recursive Length Prefix (RLP) encoders
-   abi - Application Binary Interface (ABI) encoders
-   crypto - cryptographic library for transaction signing and key/wallet management in Ethereum
-   tuples - Simple tuples library
-   core - Much like the previous web3j core artifact without the code generators
-   codegen - code generators
-   console - command-line tools

The below modules only depend on the core module.

-   geth - Geth specific JSON-RPC module
-   parity - Parity specific JSON-RPC module
-   infura - Infura specific HTTP header support
-   contracts - Support for specific EIP's (Ethereum Improvement Proposals)

For most use cases (interacting with the network and smart contracts) the *core* module should be all you need. The dependencies of the core module are very granular and only likely to be of use if your project is
focussed on a very specific interaction with the Ethereum network (such as ABI/RLP encoding, transaction signing but not submission, etc).

All modules are published to both Maven Central and Bintray, with the published artifact names using the names listed above, i.e.:

For Java:

   org.web3j: <module-name\> : <version\>

For Android:

   org.web3j: <module-name\> : <version\>-android
