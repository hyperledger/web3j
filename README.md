# web3j: Web3 Java Ðapp API

[![Build Status](https://travis-ci.org/conor10/web3j.svg?branch=master)](https://travis-ci.org/conor10/web3j)
[![codecov](https://codecov.io/gh/conor10/web3j/branch/master/graph/badge.svg)](https://codecov.io/gh/conor10/web3j)
[![Join the chat at https://gitter.im/web3j/web3j](https://badges.gitter.im/web3j/web3j.svg)](https://gitter.im/web3j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

web3j is a lightweight Java library for integrating with clients (nodes) on the Ethereum network.

[ JVM application ] + [ web3j ] <---> [ Ethereum node ]

It only has two runtime dependencies:

* Apache HTTP Client 
* Jackson Core for fast JSON serialisation/deserialisation


## Getting Started

Add the following dependency to your project:

### Maven

```
   <repositories>
     <repository>
       <id>oss.jfrog.org</id>
       <name>Repository from Bintray</name>
       <url>http://dl.bintray.com/web3j/maven</url>
     </repository>
   </repositories>

   <dependency>
     <groupId>org.web3j</groupId>
     <artifactId>core</artifactId>
     <version>0.1.0</version>
   </dependency>
```

### Gradle

```
repositories {
   maven {url "http://dl.bintray.com/web3j/maven"}
}
compile ("org.web3j:core:0.1.0")
```


Start up an Ethereum client if you don't already have one running, such as [Geth](https://github.com/ethereum/go-ethereum/wiki/geth):

geth --rpc --testnet



To send asynchronous requests using a Future:

```java
Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
String clientVersion = clientVersion.getWeb3ClientVersion();
```


To send synchronous requests:

```java
Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
String clientVersion = clientVersion.getWeb3ClientVersion();
```


## Further Details

web3j is based on specifications from the [JSON-RPC](https://github.com/ethereum/wiki/wiki/JSON-RPC) and [web3.js](https://github.com/ethereum/web3.js) projects. 

It provides type safe access to all responses. Optional or null responses are wrapped in Java 8's [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) type.

Async requests are handled using Java 8's [CompletableFutures](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html).

Quantity data types are returned as [BigInteger](https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html)'s. For simple results, you can obtain the quantity as a String via `org.web3j.protocol.jsonrpc20.Response.getResult()`.


## Tested clients

Geth
* 1.4.10-stable-5f55d95a (in progress)

More coming soon.

You can run the integration test class `org.web3j.protocol.jsonrpc20.ProtocolIT` verify other clients.


## Coming Soon

* Offline transaction signing
* IPC interface support
* WebSocket interface support


## Related projects

For a .NET implementation, check out [Nethereum](https://github.com/Nethereum/Nethereum).
 
For a pure Java implementation of the Ethereum client, check out [EthereumJ](https://github.com/ethereum/ethereumj) and the work of [Ether.Camp](https://github.com/ether-camp/).


## Thanks and Credits

* The [Nethereum](https://github.com/Nethereum/Nethereum) project for the inspiration
* [Othera](https://www.othera.com.au/) for the great things they are building on the platform
* The [Finhaus](http://finhaus.com.au/) guys for putting me onto Nethereum
* Everyone involved in the Ethererum project and its surrounding ecosystem
