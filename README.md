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

### 部署合约

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

### 调用合约函数

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

### eth_call

通过[eth_call](https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call)调用SimpleContract合约get函数，需要指定用户地址，合约地址，和编码后的参数：
```java
    String from = "0dbd369a741319fa5107733e2c9db9929093e3c7";
    Call call = new Call(from, contractAddress, "0x6d4ce63c");
    String calResult = service.ethCall(call, DefaultBlockParameter.valueOf("latest")).send().getValue();
```
具体代码可参考[SendTransactionDemo.java](https://github.com/cryptape/web3j/blob/master/examples/src/main/java/org/web3j/examples/SendTransactionDemo.java#L58)

### 使用web3j工具更方便地部署合约

web3j提供了一个可以自动生成智能合约java代码的方式，利用该代码包既可以部署合约，也可以调用合约。具体可以参考 [web3j智能合约调用](https://docs.web3j.io/smart_contracts.html#smart-contract-wrappers), 需要下载[web3j-2.3.0](https://github.com/web3j/web3j/releases/tag/v2.3.0)版本。通过生成的java代码来部署合约有两种方式:
第一种方式，通过Credentials来部署合约:
```java
YourSmartContract contract = YourSmartContract.deploy(
        <web3j>, <credentials>, quota, nonce,
        [<initialValue>,]
        <param1>, ..., <paramN>).send();
```
第二种方式，通过TransactionManager来部署合约:
```java
YourSmartContract contract = YourSmartContract.deploy(
        <web3j>, <transactionManager>, quota, nonce,
        <param1>, ..., <paramN>).send();
```
目前发送交易CITA只支持eth_sendRawTransaction, 所以只能通过第二种方式来部署合约。

### 开发流程

在根目录下执行`gradle coreJar`，然后将core/build/libs/core-version.jar作为本地依赖。

### 目前支持的接口

[CITA-JsonRpc接口](http://cita.readthedocs.io/zh_CN/latest/rpc.html)