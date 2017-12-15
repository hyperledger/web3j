## 测试工具

### 测试内容

* 功能测试
  * jsonrpc接口
  * 发送交易是否执行成功(通过代币合约，在不同账户之间转账，查看交易receipt和账户的余额是否正确)
* 性能测试
  * jsonrpc性能测试(查询链高度，发送交易)
  * 交易入链性能测试
  * 获取交易receipt

### 运行

#### 功能测试

1.jsonrpc接口测试：
```
gradle interfaceTest
```
或者先打成jar包
```shell
gradle benchJar
cd ./benchmark/build/libs
java -jar benchmark-3.1.1.jar -interface
```
2.代币合约测试
```
gradle tokenTest
```
默认的配置文件为./benchmark/src/main/resources/token_test_confg.json，也可以打包为jar包之后指定配置文件:
```shell
gradle benchJar
cd ./benchmark/build/libs
java -jar benchmark-3.1.1.jar -token ../../src/main/resources/token_test_config.json
```

#### 性能测试

性能测试的默认配置文件为./benchmark/src/main/resources/config.json，配置文件中的type字段用来表示测试类型:

* BlockNumber  获取当前链高度
* SendTransaction  发送交易
* Analysis 根据SendTransaction的结果分析链上交易
* SendTransactionAndGetReceipt 发送交易并获取receipt，统计交易执行成功个数

直接在根目录运行:
```
gradle benchTest
```
或者自己指定配置文件:
```
gradle benchJar
cd ./benchmark/build/libs
java -jar benchmark-3.1.1.jar -config ../../src/main/resources/config.json
```
测试结果会以json的形式输出到console,并且会保存到相应的json文件中。