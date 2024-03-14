# Change Log

All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

# [4.11.0](https://github.com/web3j/web3j/compare/v4.10.3...v4.11.0) (2024-02-14)

### Bug Fixes

* **Integration Tests:** Fixed all the failing and skipped Intergration tests specially Besu privacy tests [#1958](https://github.com/web3j/web3j/pull/1958)
* Fixed Dynamic Arrays encoder [#1961](https://github.com/web3j/web3j/pull/1961)
* Fixed dynamic arrays decoder [#1974](https://github.com/web3j/web3j/pull/1974)
* Fixed generateJavaFiles ArrayInStruct [#1962](https://github.com/web3j/web3j/pull/1962)
* Fixed encoding of structs without members [#1968](https://github.com/web3j/web3j/pull/1968)
* Fixed java reserved words codegen errors [#1975](https://github.com/web3j/web3j/pull/1975)

### Features

* **EIP-4844:** Added Support for sending EIP-4844 Blob Transactions [#2000](https://github.com/web3j/web3j/pull/2000)
* Added yParity for Geth compatibility [#1959](https://github.com/web3j/web3j/pull/1959)
* Sepolia network added [#1971](https://github.com/web3j/web3j/pull/1971)
* Adding support for EIP1559 Private Transactions [#1980](https://github.com/web3j/web3j/pull/1980)
* Add AccessList to 1559 transaction rlp encoding [#1992](https://github.com/web3j/web3j/pull/1992)

### BREAKING CHANGES

* NIL