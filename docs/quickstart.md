Quickstart
==========

Web3j CLI
---------

Install the Web3j binary.

To get the latest version on Mac OS or Linux, type the following in your terminal:

```bash
curl -L https://get.web3j.io | sh
```

Then to create a new project, simply run:

```bash
web3j new
```

Or, to import an existing Solidity project into Web3j, run:

```bash
web3j import
```

Then to build your project run:

```bash
./gradle build
```

For more information on using the Web3j CLI, head to the [CLI section](command_line_tools.md).


Sample project
--------------

A [web3j sample project](https://github.com/web3j/sample-project-gradle) is available that demonstrates a number of core features of Ethereum with web3j, including:

-   Connecting to a node on the Ethereum network
-   Loading an Ethereum wallet file
-   Sending Ether from one address to another
-   Deploying a smart contract to the network
-   Reading a value from the deployed smart contract
-   Updating a value in the deployed smart contract
-   Viewing an event logged by the smart contract
