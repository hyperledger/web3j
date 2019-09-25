Smart Contracts
===============

Developers have the choice of three languages for writing smart contracts:

[Solidity](https://Solidity.readthedocs.io/)

> The flagship language of Ethereum, and most popular language for smart contracts.

[Serpent](https://github.com/ethereum/wiki/wiki/Serpent)

> A Python like language for writing smart contracts.

LISP Like Language (LLL)

> A low level language, Serpent provides a superset of LLL. There's not a great deal of information for       working with LLL, the following blog [/var/log/syrinx](http://blog.syrinx.net/) and associated [lll-resurrected GitHub](https://github.com/zigguratt/lll-resurrected) repository is a  good place to start.

In order to deploy a smart contract onto the Ethereum blockchain, it must first be compiled into a bytecode format, then it can be sent as part of a transaction. web3j can do all of this for you with its [Solidity smart contract wrappers](#solidity-smart-contract-wrappers). To understand what is happening behind the scenes, you can refer to the details in [Creation of a smart contract](transactions.md#creation-of-a-smart-contract).

Given that Solidity is the language of choice for writing smart contracts, it is the language supported by web3j, and is used for all subsequent examples.

Getting started with Solidity
-----------------------------

An overview of Solidity is beyond the scope of these docs, however, the following resources are a good place to start:

-   [Contract Tutorial](https://github.com/ethereum/go-ethereum/wiki/Contract-Tutorial) on the Go Ethereum Wiki
-   [Introduction to Smart Contracts](http://Solidity.readthedocs.io/en/develop/introduction-to-smart-contracts.html) in the Solidity project documentation
-   [Writing a contract](https://ethereum-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#writing-a-contract) in the Ethereum Homestead Guide

Compiling Solidity source code 
------------------------------

Compilation to bytecode is performed by the Solidity compiler, *solc*. You can install the compiler, locally following the instructions as per [the project documentation](http://solidity.readthedocs.io/en/develop/installing-solidity.html).

To compile the Solidity code run:

``` bash
$ solc <contract>.sol --bin --abi --optimize -o <output-dir>/
```

The *--bin* and *--abi* compiler arguments are both required to take full advantage of working with smart contracts from web3j.

*--bin*

 Outputs a Solidity binary file containing the hex-encoded binary to provide with the transaction request. This is required only for *deploy* and *isValid* [Solidity smart contract wrappers](#solidity-smart-contract-wrappers) methods.

*--abi*

 Outputs a Solidity [Application Binary Interface](application_binary_interface.md) file which details all of the publicly accessible contract methods and their associated parameters. These details along with the contract address are crucial for interacting with smart contracts. The ABI file is also used for the generation of [Solidity smart contract wrappers](#solidity-smart-contract-wrappers)

There is also a *--gas* argument for providing estimates of the [Gas](transactions.md#gas) required to create a contract and transact with its methods.

Alternatively, you can write and compile Solidity code in your browser via the [browser-solidity](https://remix.ethereum.org/#optimize=false&evmVersion=null&version=soljson-v0.5.1+commit.c8a2cb62.js) project. browser-solidity is great for smaller smart contracts, but you may run into issues working with larger contracts.

You can also compile Solidity code via Ethereum clients such as Geth and Parity, using the JSON-RPC method [eth_compileSolidity](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_compileSolidity) which is also supported in web3j. However, the Solidity compiler must be
installed on the client for this to work.

There are further options available, please refer to the [relevant section](https://ethereum-homestead.readthedocs.io/en/latest/contracts-and-transactions/contracts.html#compiling-a-contract) in the Homestead documentation.

Deploying and interacting with smart contracts
----------------------------------------------

If you want to avoid the underlying implementation detail for working with smart contracts, web3j provides [Solidity smart contract wrappers](#solidity-smart-contract-wrappers) which enable you to interact directly with all of a smart contract's methods via a generated wrapper object.

Alternatively, if you wish to send regular transactions or have more control over your interactions with your smart contracts, please refer to the sections [Solidity smart contract wrappers](#solidity-smart-contract-wrappers), [Transacting with a smart contract](transactions.md#transacting-with-a-smart-contract) and [Querying the state of a smart contract](transactions.md#querying-the-state-of-a-smart-contract) for details.

Smart contract examples
-----------------------

web3j provides a number of smart contract examples in the project directory [codegen/src/test/resources/solidity](https://github.com/web3j/web3j/tree/master/codegen/src/test/resources/solidity)

It also provides integration tests for demonstrating the deploying and working with those smart contracts in the [integration-tests/src/test/java/org/web3j/protocol/scenarios](https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios) module.

![image](img/smart_contract.png)

EIP-20 Ethereum token standard smart contract 
---------------------------------------------

There an Ethereum standard, [EIP-20](https://github.com/ethereum/EIPs/issues/20) which started off as an [Ethereum Improvement Proposal
(EIP)](https://github.com/ethereum/EIPs), that defines the standard functions that a smart contract providing tokens should implement.

The EIP-20 standard provides function definitions, but does not provide an implementation example. However, there is an implementation provided in
[codegen/src/test/resources/solidity/contracts](https://github.com/web3j/web3j/tree/master/codegen/src/test/resources/solidity/contracts), which has been taken from ConsenSys' [Tokens project](https://github.com/ConsenSys/Tokens).

Open Zepplin also provide an example implementation on [GitHub](https://github.com/OpenZeppelin/zeppelin-solidity/tree/master/contracts/token).

There are two integration tests that have been written to fully demonstrate the functionality of this token smart contract.

[HumanStandardTokenGeneratedIT](https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/HumanStandardTokenGeneratedIT.java) uses the generated HumanStandardTokenGenerated [Solidity smart contract wrappers](#solidity-smart-contract-wrappers) to demonstrate this.

Alternatively, if you do not wish to use a smart contract wrapper and would like to work directly with the underlying JSON-RPC calls, please refer to [HumanStandardTokenIT](https://github.com/web3j/web3j/tree/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/HumanStandardTokenIT.java).

Solidity smart contract wrappers
--------------------------------

web3j supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

The web3j [Command Line Tools](command_line_tools.md) tools ship with a command line utility for generating the smart contract function wrappers:

``` bash
$ web3j solidity generate [-hV] [-jt] [-st] -a=<abiFile> [-b=<binFile>] -o=<destinationFileDir> -p=<packageName>

   -h, --help                        Show this help message and exit.
   -V, --version                     Print version information and exit.
   -jt, --javaTypes                  use native java types. Default: true
   -st, --solidityTypes              use solidity types.
   -a, --abiFile=<abiFile>           abi file with contract definition.
   -b, --binFile=<binFile>           optional bin file with contract compiled code in order to generate deploy methods.
   -o, --outputDir=<destinationFileDir> destination base directory.
   -p, --package=<packageName>       base package name.
```

BinFile is required for [Contract validity](#contract-validity)

In versions prior to 3.x of web3j, the generated smart contract wrappers used native Solidity types. From web3j 3.x onwards, Java types are created by default. You can create Solidity types using the *--solidityTypes* command line argument.

You can also generate the wrappers by calling the Java class directly:

``` bash
org.web3j.codegen.SolidityFunctionWrapperGenerator -b /path/to/<smart-contract>.bin -a /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name
```

Where the *bin* and *abi* are obtained as per [Compiling Solidity sourse code](#compiling-solidity-source-code)

The native Java to Solidity type conversions used are detailed in the [Application Binary Interface](application_binary_interface.md) section.

The smart contract wrappers support all common operations for working with smart contracts:

-   [Construction and deployment](#construction-and-deployment)
-   [Invoking transactions and events](#invoking-transactions-and-events)
-   [Calling constant methods](#calling-constant-methods)
-   [Contract validity](#contract-validity)

Any method calls that requires an underlying JSON-RPC call to take place will return a Future to avoid blocking.

web3j also supports the generation of Java smart contract function wrappers directly from [Truffle's](http://truffleframework.com/) [Contract
Schema](https://github.com/trufflesuite/truffle/tree/develop/packages/truffle-contract-schema) via the [Command Line Tools](command_line_tools.md) utility.

``` bash
$ web3j truffle generate [--javaTypes|--solidityTypes] /path/to/<truffle-smart-contract-output>.json -o /path/to/src/main/java -p com.your.organisation.name
```

And this also can be invoked by calling the Java class:

``` bash
org.web3j.codegen.TruffleJsonFunctionWrapperGenerator /path/to/<truffle-smart-contract-output>.json -o /path/to/src/main/java -p com.your.organisation.name
```

A wrapper generated this way is "enhanced" to expose the per-network deployed address of the contract. These addresses are from the truffle deployment at the time the wrapper is generared.

Construction and deployment
---------------------------

Construction and deployment of smart contracts happens with the *deploy* method:

```java
YourSmartContract contract = YourSmartContract.deploy(
        <web3j>, <credentials>, GAS_PRICE, GAS_LIMIT,
        [<initialValue>,]
        <param1>, ..., <paramN>).send();
```

This will create a new instance of the smart contract on the Ethereum blockchain using the supplied credentials, and constructor parameter values.

The *<initialValue\>* parameter is only required if your smart contract accepts Ether on construction. This requires the Solidity [payable](https://solidity.readthedocs.io/en/v0.5.10/contracts.html#function-modifiers) modifier to be present in the contract.

It returns a new smart contract wrapper instance which contains the underlying address of the smart contract. If you wish to construct an instance of a smart contract wrapper with an existing smart contract,
simply pass in it's address:

```java
YourSmartContract contract = YourSmartContract.load(
        "0x<address>|<ensName>", web3j, credentials, GAS_PRICE, GAS_LIMIT);
```

Contract validity
-----------------

Using this method, you may want to ascertain that the contract address that you have loaded is the smart contract that you expect. For this you can use the *isValid* smart contract method, which will only return true
if the deployed bytecode at the contract address matches the bytecode in the smart contract wrapper.:

```java
contract.isValid();  // returns false if the contract bytecode does not match what's deployed
                     // at the provided address
```

Note: Contract wrapper has to be generated with *--bin* for this to work.

Transaction Managers
--------------------

web3j provides a [TransactionManager](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/TransactionManager.java) abstraction to control the manner you connect to Ethereum clients with. The default mechanism uses web3j's [RawTransactionManager](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/RawTransactionManager.java) which works with Ethereum wallet files to sign transactions offline before submitting them to the network.

However, you may wish to modify the transaction manager, which you can pass to the smart contract deployment and creation methods instead of a credentials object, i.e.:

```java
YourSmartContract contract = YourSmartContract.deploy(
        <web3j>, <transactionManager>, GAS_PRICE, GAS_LIMIT,
        <param1>, ..., <paramN>).send();
```

In addition to the RawTransactionManager, web3j provides a [ClientTransactionManager](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/ClientTransactionManager.java) which passes the responsibility of signing your transaction on to the Ethereum client you are connecting to.

There is also a [ReadonlyTransactionManager](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/ReadonlyTransactionManager.java) for when you only want to retrieve data from a smart contract, but not transact with it.

Specifying the Chain Id on Transactions (EIP-155)
-------------------------------------------------

The RawTransactionManager takes an optional *chainId* parameter to specify the chain id to be used on transactions as per [EIP-155](https://github.com/ethereum/EIPs/issues/155). This prevents transactions from one chain being re-broadcast onto another chain, such as from Ropsten to Mainnet:

```java
TransactionManager transactionManager = new RawTransactionManager(
        web3j, credentials, ChainId.MAINNET);
```

In order to avoid having to change config or code to specify which chain you are working with, web3j's default behaviour is to not specify chain ids on transactions to simplify working with the library. However, the recommendation of the Ethereum community is to use them.

You can obtain the chain id of the network that your Ethereum client is connected to with the following request:

```java
web3j.netVersion().send().getNetVersion();
```

Transaction Receipt Processors
------------------------------

By default, when a new transaction is submitted by web3j to an Ethereum client, web3j will continually poll the client until it receives a [TransactionReceipt](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/protocol/core/methods/response/TransactionReceipt.java), indicating that the transaction has been added to the blockchain. If you are sending a number of transactions asynchronously with web3j, this can result in a number of threads polling the client concurrently.

To reduce this polling overhead, web3j provides configurable [TransactionReceiptProcessors](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/TransactionReceiptProcessor.java).

There are a number of processors provided in web3j:

-   [PollingTransactionReceiptProcessor](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/PollingTransactionReceiptProcessor.java) is the default processor used in web3j, which polls periodically for a transaction receipt for each individual pending transaction.
-   [QueuingTransactionReceiptProcessor](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/QueuingTransactionReceiptProcessor.java) has an internal queue of all pending transactions. It contains a
    worker that runs periodically to query if a transaction receipt is available yet. If a receipt is found, a callback to the client is invoked.
-   [NoOpProcessor](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/NoOpProcessor.java) provides an [EmptyTransactionReceipt](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/EmptyTransactionReceipt.java) to clients which only contains the transaction hash. This is for clients who do not want web3j to perform any polling for a transaction receipt.

**Note:** the
[EmptyTransactionReceipt](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/EmptyTransactionReceipt.java) is also provided in the the initial response from the [QueuingTransactionReceiptProcessor](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/QueuingTransactionReceiptProcessor.java). This allows the caller to have the transaction hash for the transaction
that was submitted to the network.

If you do not wish to use the default processor([PollingTransactionReceiptProcessor](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/PollingTransactionReceiptProcessor.java)), you can specify the transaction receipt processor to use as follows:

```java
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
        web3j, credentials, ChainId.MAINNET, transactionReceiptProcessor);
```

If you require further information, the [FastRawTransactionManagerIT](https://github.com/web3j/web3j/blob/master/integration-tests/src/test/java/org/web3j/protocol/scenarios/FastRawTransactionManagerIT.java) demonstrates the polling and queuing approaches.

Invoking transactions and events 
--------------------------------

All transactional smart contract methods are named identically to their Solidity methods, taking the same parameter values. Transactional calls do not return any values, regardless of the return type specified on the method. Hence, for all transactional methods the [Transaction Receipt](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt) associated with the transaction is returned.:

```java
TransactionReceipt transactionReceipt = contract.someMethod(
             <param1>,
             ...).send();
```

The transaction receipt is useful for two reasons:

1.  It provides details of the mined block that the transaction resides in
2.  [Solidity events](http://Solidity.readthedocs.io/en/develop/contracts.html?highlight=events#events) that are called will be logged as part of the transaction, which can then be extracted

Any events defined within a smart contract will be represented in the smart contract wrapper with a method named *process<Event Name\>Event*, which takes the Transaction Receipt and from this extracts the indexed and non-indexed event parameters, which are returned decoded in an
instance of the [EventValues](https://github.com/web3j/web3j/blob/master/abi/src/main/java/org/web3j/abi/EventValues.java) object.:

```java
EventValues eventValues = contract.processSomeEvent(transactionReceipt);
```

Alternatively you can use an Flowable filter instead which will listen for events associated with the smart contract:

```java
contract.someEventFlowable(startBlock, endBlock).
        .subscribe(event -> ...);
```

For more information on working with Flowable filters, refer to [Filters and Events](filters_and_events.md).

**Remember** that for any indexed array, bytes and string Solidity parameter types, a Keccak-256 hash of their values will be returned, see the [documentation](http://Solidity.readthedocs.io/en/latest/contracts.html#events) for further information.

Calling constant methods
------------------------

Constant methods are those that read a value in a smart contract, and do not alter the state of the smart contract. These methods are available with the same method signature as the smart contract they were generated from:

```java
Type result = contract.someMethod(<param1>, ...).send();
```

Dynamic gas price and limit 
---------------------------

When working with smart contracts you may want to specify different gas price and limit values depending on the function being invoked. You can do that by creating your own [ContractGasProvider](https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/gas/ContractGasProvider.java) for the smart contract wrapper.

Every generated wrapper contains all smart contract method names listed as a constants, which facilitates compilation-time matching via a *switch* statement.

For example, using the [Greeter](https://github.com/web3j/web3j/blob/master/codegen/src/test/resources/solidity/greeter/Greeter.sol) contract:

```java
Greeter greeter = new Greeter(...);
greeter.setGasProvider(new DefaultGasProvider() {
    @Override
    public BigInteger getGasPrice(String contractFunc) {
        switch (contractFunc) {
            case Greeter.FUNC_GREET: return BigInteger.valueOf(22_000_000_000L);
            case Greeter.FUNC_KILL: return BigInteger.valueOf(44_000_000_000L);
            default: throw new NotImplementedException();
        }
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        switch (contractFunc) {
            case Greeter.FUNC_GREET: return BigInteger.valueOf(4_300_000);
            case Greeter.FUNC_KILL: return BigInteger.valueOf(5_300_000);
            default: throw new NotImplementedException();
        }
    }
});
```

Examples
--------

Please refer to [EIP-20 Ethereum token standard smart contract](#eip-20-ethereum-token-standard-smart-contract)
