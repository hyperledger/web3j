Solidity Smart Contract Wrappers
================================

The library supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

This can be achieved by running:

.. code-block:: bash

   org.web3j.codegen.SolidityFunctionWrapperGenerator /path/to/<smart-contract>.abi -o /path/to/src/dir/java -p com.your.organisation.name

See `org.web3j.protocol.scenarios.FunctionWrappersIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/FunctionWrappersIT.java>`_ for an example of using a generated smart contract Java wrapper.

Note - at present the wrappers invoke smart contracts via `EthCall <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call>`_, so a transaction does not take place. Transaction support is imminent.

For details of how to call smart contracts via transactions, refer to `org.web3j.protocol.scenarios.DeployContractIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/DeployContractIT.java>`_.