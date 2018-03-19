# web3j

## 环境

### 1、java

```
$java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

### 2、gradle

```
$gradle -version

------------------------------------------------------------
Gradle 4.3
------------------------------------------------------------

Build time:   2017-10-30 15:43:29 UTC
Revision:     c684c202534c4138b51033b52d871939b8d38d72

Groovy:       2.4.12
Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
JVM:          1.8.0_144 (Oracle Corporation 25.144-b01)
OS:           Linux 4.10.0-35-generic amd64

```

## web3j简介

web3j发送交易的方式是通过封装 [JSON-PRC](https://github.com/ethereum/wiki/wiki/JSON-RPC) API 来实现的，web3j提供了支持http和IPC的通信方式，目前的改造仅支持http的方式发送CITA交易。 发送交易成功后，web3j还对JSON-RPC的响应结果进行了封装，针对不同的交易结果设置了不同的响应对象。

使用web3j部署合约和调用合约方法有三种方式，一种是本地编译合约文件，手动拼接参数，调用sendTransaction方法实现合约部署和方法调用；第二种是利用web3j提供的代码生成工具，将Solidity合约文件自动映射生成Java对应的合约文件；第三种方式是调用web3j提供的通用方法，只需要传入相应的参数即可，web3j会根据参数和方法组装好相应的交易请求，既不需要手动拼接参数，也不用生成额外的代码。

### 手动拼接参数部署和调用合约

##### 部署合约

类似于以太坊，在CITA中，合约的部署也要通过发送交易。CITA的Transaction定义在[Transaction.java](https://github.com/cryptape/web3j/blob/master/core/src/main/java/org/web3j/protocol/core/methods/request/Transaction.java)文件中, Transaction有3个特殊的参数:

*  **nonce**: 可以随机生成，或者根据具体业务生成。
*  **quota**: 类似于ethereum中的gas。
*  **valid_until_block**: 超时机制，假定当前链高度为h, 则valid_until_block取值应该为(h, h+100]之间的任意数值。

合约代码(test_example.sol)：
```solidity
pragma solidity ^0.4.14;

contract SimpleStorage {
    uint storedData;

    function set(uint x) {
        storedData = x;
    }

    function get() constant returns (uint) {
        return storedData;
    }
}
```

运行如下命令得到合约二进制代码
```shell
$solc test_example.sol --bin
======= test_example.sol:SimpleStorage =======
Binary:
6060604052341561000f57600080fd5b60cb8061001d6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360fe47b11460465780636d4ce63c14606657600080fd5b3415605057600080fd5b60646004808035906020019091905050608c565b005b3415607057600080fd5b60766096565b6040518082815260200191505060405180910390f35b8060008190555050565b600080549050905600a165627a7a723058208e89b7ff1b7f21f2685af794d94f0e3e77e00ae238f705b0a606cf4d5d37994f0029
```

构造交易:
```java
    // get block number of chain
    long currentHeight = currentBlockNumber();
    long validUntilBlock = currentHeight + 80;
    BigInteger nonce = BigInteger.valueOf(Math.abs(random.nextLong()));
    long quota = 1000000;
    Transaction tx = Transaction.createContractTransaction(nonce, quota, validUntilBlock, contractCode);
```
CITA只支持eth_sendRawTransaction，需要用户对交易签名：
```java
    String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
    String rawTx = tx.sign(privateKey);
```
发送交易：
```java
    // please specific your url
    Web3j service = Web3j.build(new HttpService("http://127.0.0.1:1337"));
    EthSendTransaction result = service.ethSendRawTransaction(rawTx).send();
```
具体调用可参考[SendTransactionDemo.java](https://github.com/cryptape/web3j/blob/master/examples/src/main/java/org/web3j/examples/SendTransactionDemo.java#L27)

##### 调用合约函数

现在我们要调用SimpleContract的set函数，调用合约需要指定合约地址，通过上面部署合约时得到的交易hash，我们可以获取部署合约交易的[Receipt](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt)，receipt中保存我们部署的合约地址:
```java
    String txHash = result.getSendTransactionResult().getHash();
    TransactionReceipt txReceipt = service.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().get();
    String contractAddress = txReceipt.getContractAddress();
```
调用合约方法需要指定合约地址，合约函数和参数，其中合约函数和参数是通过[contract ABI](https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI)方式编码为data参数，set函数和参数1的编码结果为60fe47b10000000000000000000000000000000000000000000000000000000000000001，构造交易如下：
```java
    String functionCallData = "60fe47b10000000000000000000000000000000000000000000000000000000000000001";
    String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
    long currentHeight = currentBlockNumber();
    // validUntilBlock should between currentHeight and currentHeight+100
    long validUntilBlock = currentHeight + 80;
    BigInteger nonce = BigInteger.valueOf(Math.abs(random.nextLong()));
    long quota = 1000000;
    Transaction tx = Transaction.createFunctionCallTransaction(contractAddress, nonce, quota, validUntilBlock, functionCallData);
    String rawTx = tx.sign(privateKey);
    String txHash =  service.ethSendRawTransaction(rawTx).send().getSendTransactionResult().getHash();
```
具体代码可参考[SendTransactionDemo.java](https://github.com/cryptape/web3j/blob/master/examples/src/main/java/org/web3j/examples/SendTransactionDemo.java#L43)

##### eth_call

通过[eth_call](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call)调用SimpleContract合约get函数，需要指定用户地址，合约地址，和编码后的参数：
```java
    String from = "0dbd369a741319fa5107733e2c9db9929093e3c7";
    Call call = new Call(from, contractAddress, "0x6d4ce63c");
    String calResult = service.ethCall(call, DefaultBlockParameter.valueOf("latest")).send().getValue();
```
具体代码可参考[SendTransactionDemo.java](https://github.com/cryptape/web3j/blob/master/examples/src/main/java/org/web3j/examples/SendTransactionDemo.java#L58)

### 使用web3j工具更方便地部署合约

web3j提供了一个可以自动生成智能合约java代码的方式，利用该代码包既可以部署合约，也可以调用合约。为了适配CITA的交易执行，我们修改了代码生成工具。在web3j目录下运行`gradle shadowJar`命令，在`console/build/libs/`目录下会生成console-version-all.jar(当前为console-3.2.0-all.jar)，包含了代码生成工具。使用方式如下:
```shell
$ java -jar console-3.2.0-all.jar solidity generate [--javaTypes|--solidityTypes] /path/to/<smart-contract>.bin /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name
```

以[benchmark](https://github.com/cryptape/web3j/tree/master/benchmark/src/main/resources)目录下的Token.sol, Token.bin, Token.abi文件为例，代码生成命令为：

```shell
java -jar console/build/libs/console-3.2.0-all.jar solidity generate benchmark/src/main/resources/Token.bin benchmark/src/main/resources/Token.abi -o benchmark/src/main/java/ -p org.web3j.benchmark
```

通过代码生成工具，生成[Token.java](https://github.com/cryptape/web3j/blob/master/benchmark/src/main/java/org/web3j/benchmark/Token.java)文件。发送交易相关的接口如deploy和合约方法有额外的`quota`, `nonce`和`invalidUntilBlock`，这三个参数是发送交易必需的(如前所述)。`TransactionManager`必须使用[CitaTransactionManager](https://github.com/cryptape/web3j/blob/master/core/src/main/java/org/web3j/tx/CitaTransactionManager.java)。

### 使用通用方法完成部署和调用

web3j提供了通用的合约部署和合约方法调用接口，只需要传入相关的参数即可，如合约文件、合约地址、合约方法名以及合约方法参数等，这种方式既不需要手动拼接参数，也不需要生成额外的Java代码。

部署合约的通用方法定义如下：

```java
// 需要指定合约文件、nonce、quota参数，返回值包含交易hash和状态
public EthSendTransaction deploy(File contractFile, BigInteger nonce, BigInteger quota)

public CompletableFuture<EthSendTransaction> deployAsync(File contractFile, BigInteger nonce, BigInteger quota)
```

调用合约方法的通用方法定义如下：

```java
// 需要指定合约地址、合约方法、nonce、quota参数以及合约方法本身需要的参数，返回值即为Solidity合约方法的返回值
public Object callContract(String contractAddress, String funcName, BigInteger nonce, BigInteger quota, Object... args)

// functionAbi为合约方法的封装，包含了合约方法名称、入参类型、返回值类型以及其他属性
public Object callContract(String contractAddress, AbiDefinition functionAbi, BigInteger nonce, BigInteger quota, Object... args)
```

首次部署合约时，需要指定合约文件，web3j会将编译生成的abi发送至链上，以后再调用该合约的方法时，就不需要再指定合约文件，web3j会根据合约地址先行获取abi，并完成合约文件的映射，具体使用方式可以参考Java示例代码`tests/src/main/java/org/web3j/tests/TokenTest.java`。

### 开发流程

在根目录下执行`gradle coreJar`，然后将core/build/libs/core-version.jar作为本地依赖。

### 目前支持的接口

[CITA-JsonRpc接口](http://cita.readthedocs.io/zh_CN/latest/rpc.html)