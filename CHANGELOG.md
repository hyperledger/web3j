# Change Log

All notable changes to this project will be documented in this file.
See [Conventional Commits](https://conventionalcommits.org) for commit guidelines.

# [4.11.3]() (Upcoming)

### Bug Fixes

* Fix for test wrappers generation [#2025](https://github.com/web3j/web3j/pull/2025)
* Fix Snapshot release secrets [#2031](https://github.com/hyperledger/web3j/pull/2031)
* Fix Sign method [#2033](https://github.com/hyperledger/web3j/pull/2033)

### Features

* Bump snapshot version to 4.11.3 [#2024](https://github.com/web3j/web3j/pull/2024)

### BREAKING CHANGES

*


# [4.11.2](https://github.com/web3j/web3j/releases/tag/v4.11.2) (2024-03-27)

### Bug Fixes

* Bug fix for large binary with unlink libraries codegen [#2016](https://github.com/web3j/web3j/pull/2016)
* Fix contract wrapper generation [#2017](https://github.com/web3j/web3j/pull/2017)
* Fix for test java wrappers with duplicate name [#2020](https://github.com/web3j/web3j/pull/2020)

### Features

* Changelog entry and PR template edited [#2021](https://github.com/web3j/web3j/pull/2021)
* bump snapshot version to 4.11.2  [#2015](https://github.com/web3j/web3j/pull/2015)
* Enrich generateBothCallAndSend feature in TruffleJsonFunctionWrapperGenerator [#1986](https://github.com/web3j/web3j/pull/1986)

### BREAKING CHANGES

* 


# [4.11.1](https://github.com/web3j/web3j/releases/tag/v4.11.1) (2024-03-14)

### Bug Fixes

* fix versionedHashes to blobVersionedHashes [#2009](https://github.com/web3j/web3j/pull/2009)
* Fix typos [#2010](https://github.com/web3j/web3j/pull/2010)

### Features

* **EIP-4844:** Calculate baseFeePerBlobGas value and other EIP4844 changes [#2006](https://github.com/web3j/web3j/pull/2006)
* **EIP-4788:** add parentBeaconBlockRoot field [#2007](https://github.com/web3j/web3j/pull/2007)
* Add function to link binary with reference libraries in wrappers contract deployment [#1988](https://github.com/web3j/web3j/pull/1988)
* Added files for repo migration [#2011](https://github.com/web3j/web3j/pull/2011)

### BREAKING CHANGES

* NIL


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