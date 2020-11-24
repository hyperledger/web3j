Web3j: Web3 Java Ethereum Ðapp API
==================================

[![Documentation Status](https://img.shields.io/travis/web3j/web3j-docs?label=docs)](https://docs.web3j.io/)
[![Build Status](https://travis-ci.org/web3j/web3j.svg?branch=master)](https://travis-ci.org/web3j/web3j)
[![codecov](https://codecov.io/gh/web3j/web3j/branch/master/graph/badge.svg)](https://codecov.io/gh/web3j/web3j)
[![Join the chat at https://gitter.im/web3j/web3j](https://img.shields.io/discourse/users?server=https%3A%2F%2Fcommunity.web3labs.com)](https://community.web3labs.com)


Web3j is a lightweight, highly modular, reactive, type safe Java and
Android library for working with Smart Contracts and integrating with
clients (nodes) on the Ethereum network:

![image](https://github.com/web3j/web3j-docs/blob/master/docs/img/web3j_network.png)

This allows you to work with the [Ethereum](https://www.ethereum.org/)
blockchain, without the additional overhead of having to write your own
integration code for the platform.

The [Java and the Blockchain](https://www.youtube.com/watch?v=ea3miXs_P6Y) talk provides
an overview of blockchain, Ethereum and Web3j.

Features
--------

-   Complete implementation of Ethereum's
    [JSON-RPC](https://github.com/ethereum/wiki/wiki/JSON-RPC) client
    API over HTTP and IPC
-   Ethereum wallet support
-   Auto-generation of Java smart contract wrappers to create, deploy,
    transact with and call smart contracts from native Java code
    ([Solidity](http://solidity.readthedocs.io/en/latest/using-the-compiler.html#using-the-commandline-compiler)
    and
    [Truffle](https://github.com/trufflesuite/truffle-contract-schema)
    definition formats supported)
-   Reactive-functional API for working with filters
-   [Ethereum Name Service (ENS)](https://ens.domains/) support
-   Support for Parity's
    [Personal](https://github.com/paritytech/parity/wiki/JSONRPC-personal-module),
    and Geth's
    [Personal](https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal)
    client APIs
-   Support for [Infura](https://infura.io/), so you don't have to run
    an Ethereum client yourself
-   Comprehensive integration tests demonstrating a number of the above
    scenarios
-   Command line tools
-   Android compatible
-   Support for JP Morgan's Quorum via
    [web3j-quorum](https://github.com/web3j/quorum)
-   Support for [EEA Privacy features as described in EEA
    documentation](https://entethalliance.org/technical-documents/) and
    implemented in [Hyperledger
    Besu](https://besu.hyperledger.org/en/latest/Reference/API-Methods/#eea-methods).

It has five runtime dependencies:

-   [RxJava](https://github.com/ReactiveX/RxJava) for its
    reactive-functional API
-   [OKHttp](https://hc.apache.org/httpcomponents-client-ga/index.html)
    for HTTP connections
-   [Jackson Core](https://github.com/FasterXML/jackson-core) for fast
    JSON serialisation/deserialization
-   [Bouncy Castle](https://www.bouncycastle.org/) ([Spongy
    Castle](https://rtyley.github.io/spongycastle/) on Android) for
    crypto
-   [Jnr-unixsocket](https://github.com/jnr/jnr-unixsocket) for \*nix
    IPC (not available on Android)

It also uses [JavaPoet](https://github.com/square/javapoet) for
generating smart contract wrappers.

QuickStart
---------
The simplest way to start your journey with Web3j is to create a project.
We provide this functionality using the [Epirus CLI](https://docs.epirus.io/quickstart/#installation). This latter can be installed as follows:

For Unix:

```shell script
curl -L get.epirus.io | sh && source ~/.epirus/source.sh
```

For Windows, in Powershell:

```shell script
Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/epirus-io/epirus-installer/master/installer.ps1'))
```

Create a new project by running:

```shell script
$ epirus new 
```

Or use our [Maven](https://github.com/web3j/web3j-maven-plugin) or [Gradle](https://github.com/web3j/web3j-gradle-plugin) plugins.


#### Please head to the [Web3j Documentation](https://docs.web3j.io) for further instructions on using Web3j.

Maven
-----

Java:

```
<dependency>
  <groupId>org.web3j</groupId>
  <artifactId>core</artifactId>
  <version>4.8.1</version>
</dependency>
```

Android:

```
<dependency>
  <groupId>org.web3j</groupId>
  <artifactId>core</artifactId>
  <version>4.6.0-android</version>
</dependency>
```

Gradle
------

Java:

```
compile ('org.web3j:core:4.7.0')
```

Android:

```
compile ('org.web3j:core:4.6.0-android')
```

Build instructions
------------------

Web3j includes integration tests for running against a live Ethereum
client. If you do not have a client running, you can exclude their
execution as per the below instructions.

To run a full build (excluding integration tests):

``` {.sourceCode .bash}
$ ./gradlew check
```

To run the integration tests:

``` {.sourceCode .bash}
$ ./gradlew  -Pintegration-tests=true :integration-tests:test
```

Commercial support and training
-------------------------------

Commercial support and training is available from
[web3labs.com](https://www.web3labs.com/).

License
------
Apache 2.0
