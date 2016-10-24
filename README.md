# web3j: Web3 Java Ðapp API

[![Documentation Status](https://readthedocs.org/projects/web3j/badge/?version=latest)](http://web3j.readthedocs.io/en/latest/?badge=latest)
[![Build Status](https://travis-ci.org/web3j/web3j.svg?branch=master)](https://travis-ci.org/web3j/web3j)
[![codecov](https://codecov.io/gh/web3j/web3j/branch/master/graph/badge.svg)](https://codecov.io/gh/web3j/web3j)
[![Join the chat at https://gitter.im/web3j/web3j](https://badges.gitter.im/web3j/web3j.svg)](https://gitter.im/web3j/web3j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

web3j is a lightweight, type safe Java library for integrating with clients (nodes) on the Ethereum network.

[ JVM application ] + [ web3j ] <---> [ Ethereum node ]

It can generate Java smart contract wrappers so you can interact with a smart contract like it's native Java code. 

It only has four runtime dependencies:

* [Apache HTTP Client](https://hc.apache.org/httpcomponents-client-ga/index.html)
* [Jackson Core](https://github.com/FasterXML/jackson-core) for fast JSON serialisation/deserialisation
* [Bouncy Castle](https://www.bouncycastle.org/) for crypto
* [JavaPoet](https://github.com/square/javapoet) for generating smart contract wrappers

Project documentation is available [here](https://web3j.readthedocs.io/en/latest/)


## Getting Started

Add the following dependency to your project:

### Maven

```xml
<dependency>
  <groupId>org.web3j</groupId>
  <artifactId>core</artifactId>
  <version>0.5.4</version>
</dependency>
```

### Gradle

```groovy
compile ('org.web3j:core:0.5.4')
```

Start up an Ethereum client if you don't already have one running, such as [Geth](https://github.com/ethereum/go-ethereum/wiki/geth):

`geth --rpcapi personal,db,eth,net,web3 --rpc --testnet`

Or [Parity](https://github.com/ethcore/parity)

`parity --chain testnet`



To send asynchronous requests using a Future:

```java
Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
String clientVersion = web3ClientVersion.getWeb3ClientVersion();
```


To send synchronous requests:

```java
Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
String clientVersion = web3ClientVersion.getWeb3ClientVersion();
```

To use Parity commands:
```java
Parity parity = Parity.build(new HttpService());  // defaults to http://localhost:8545/
PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
if (personalUnlockAccount.accountUnlocked()) {
    // send a transaction, or use parity.personalSignAndSendTransaction() to do it all in one
}
```


An example project is available at [web3j-example](https://github.com/web3j/web3j-example)


## Further Details

web3j is based on specifications from the [JSON-RPC](https://github.com/ethereum/wiki/wiki/JSON-RPC) and [web3.js](https://github.com/ethereum/web3.js) projects. 

Parity's [Personal JSON-RPC](https://github.com/ethcore/parity/wiki/JSONRPC-personal-module) module is supported for managing accounts, and creating transactions.

It provides type safe access to all responses. Optional or null responses are wrapped in Java 8's [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) type.

Async requests are handled using Java 8's [CompletableFutures](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html).

Quantity payload types are returned as [BigInteger](https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html)'s. For simple results, you can obtain the quantity as a String via `org.web3j.protocol.core.Response.getResult()`.


## Generating smart contract wrappers in Java

The library also supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

This can be achieved by running:

```
org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.abi -o /path/to/src/dir/java -p com.your.organisation.name
```

See `org.web3j.protocol.scenarios.FunctionWrappersIT` for an example of using a generated smart contract Java wrapper.


## Working with filters

See `org.web3j.protocol.scenarios.EventFilterIT` for an example. 


## Tested clients

Geth
* 1.4.18-stable-ef9265d0

Parity
* Parity/v1.3.8-beta/x86_64-linux-gnu/rustc1.12.0

You can run the integration test class `org.web3j.protocol.core.CoreIT` to verify clients.


## Coming Soon

* External key store support
* IPC interface support
* WebSocket interface support


## Related projects

For a .NET implementation, check out [Nethereum](https://github.com/Nethereum/Nethereum).
 
For a pure Java implementation of the Ethereum client, check out [EthereumJ](https://github.com/ethereum/ethereumj) and the work of [Ether.Camp](https://github.com/ether-camp/).


## Build instructions

web3j includes integration tests for running against a live Ethereum client. If you do not have a client running, you can exclude their execution as per the below instructions. 

To run a full build including integration tests:

```
./gradlew check
```

To run excluding integration tests:

```
./gradlew -x integrationTest check 
```


## Thanks and Credits

* The [Nethereum](https://github.com/Nethereum/Nethereum) project for the inspiration
* [Othera](https://www.othera.com.au/) for the great things they are building on the platform
* The [Finhaus](http://finhaus.com.au/) guys for putting me onto Nethereum
* [bitcoinj](https://bitcoinj.github.io/) for the reference Elliptic Curve crypto implementation 
* Everyone involved in the Ethererum project and its surrounding ecosystem
* And of course the users of the library, who've provided valuable input & feedback -
[@ice09](https://github.com/ice09), [@adridadou](https://github.com/adridadou), [@nickmelis](https://github.com/nickmelis), [@basavk](https://github.com/basavk), [@kabl](https://github.com/kabl)
