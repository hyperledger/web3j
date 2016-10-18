Transactions
============

There are three types transactions which are undertaken on the Ethereum blockchain.

These are:

#. :ref:`transfer-of-ether`
#. :ref:`creation-of-smart-contract`
#. :ref:`transacting-with-contract`

To undertake any of these transactions, it is necessary to have Ether (the fuel of the Ethereum
blockchain) residing in the Ethereum account which the transactions are taking place from. This is
to pay for the :ref:`Gas` costs, which is the transaction execution cost for the Ethereum client that
performs the transaction on your behalf, comitting the result to the Ethereum blockchain.
Instructions for obtaining Ether are described below in :ref:`obtaining-ether`.

Additionally, it is possible to query the state of a smart contract, this is described in
:ref:`querying-state`.


.. _obtaining-ether:

Obtaining Ether
---------------

To obtain Ether you have two options:

#. Mine it yourself
#. Buy Ether from another party

Mining it yourself in a private environment, or the public tet environment (testnet) is very
straight forwards. However, in the main live environment (mainnet) it requires significant
dedicated GPU time which is not likely to be feasible unless you already have a gaming PC with
multiple dedicated GPUs. If you wish to use a private environment, there is some guidance on the
`Homestead documentation <https://ethereum-homestead.readthedocs.io/en/latest/network/test-networks.html#id3>`__.

To purchase Ether you will need to go via an exchange. As different regions have different
exchanges, you will need to research the best location for this yourself. The
`Homestead documentation <https://ethereum-homestead.readthedocs.io/en/latest/ether.html#list-of-centralised-exchange-marketplaces>`__
contains a number of exchanges which is a good place to start.


Mining on testnet/private blockchains
-------------------------------------

In the Ethereum test environment (testnet), the mining difficulty is set lower then the main
environment (mainnet). This means that you can mine new Ether with a regular CPU, such as your
laptop. What you'll need to do is run an Ethereum client such as Geth or Parity to start building
up reserves. Further instructions are available on the respective sites.

Geth
  https://github.com/ethereum/go-ethereum/wiki/Mining

Parity
  https://github.com/ethcore/parity/wiki/Mining

Once you have mined some Ether, you can start transacting with the blockchain.


.. _gas:

Gas
---

When a transaction takes place in Ethereum, a transaction cost must be paid to the client that
executes the transaction on your behalf, committing the output of this transaction to the Ethereum
blockchain.

This cost is measure in gas, where gas is the number of instructions used to execute a transaction
in the Ethereum Virtual Machine. Please refer to the
`Homestead documentation <http://ethdocs.org/en/latest/contracts-and-transactions/account-types-gas-and-transactions.html?highlight=gas#what-is-gas>`__
for further information.

What this means for you when working with Ethereum clients is that there are two parameters which
are used to dictate how much Ether you wish to spend in order for a tranaction to complete:

*Gas price*

  This is the amount you are prepared in Ether per unit of gas. It defaults to a price of 9000 Wei
  (9 x 10^-15 Ether).


*Gas limit*

  This is the total amount of gas you are happy to spend on the transaction execution. There is an
  upper limit


These parameters taken together dictate the maximum amount of Ether you are willing to spend on
transaction costs. i.e. you can spend no more then *gas price * gas limit*. The gas price can also
affect how quickly a transaction takes place depending on what other transactions are available
with a more profitable gas price for miners.

You may need to adjust these parameters to ensure that transactions take place in a timely manner.


Transaction mechanisms
----------------------

When you have a valid account created with some Ether, there are two mechanisms you can use to
transact with Ethereum.

#. :ref:`signing-via-client`
#. :ref:`offline-signing`

Both mechanisms are supported via web3j.


.. _signing-via-client:

Transaction signing via an Ethereum client
-------------------------------------------

In order to transact via an Ethereum client, you first need to ensure that the client you're
transacting with knows about your wallet address. You are best off running your own Ethereum client
such as Geth/Parity in order to do this. Once you have a client running, you can create a wallet
via:

- The `Geth Wiki <https://github.com/ethereum/go-ethereum/wiki/Managing-your-accounts>`_ contains
  a good run down of the different mechanisms Geth supports such as importing private key files,
  and creating a new account via it's console
- Alternatively you can use a JSON-RPC admin command for your client, such as personal_newAccount
  for `Parity <https://github.com/ethcore/parity/wiki/JSONRPC-personal-module#personal_newaccount>`_
  or `Geth <https://github.com/ethereum/go-ethereum/wiki/Management-APIs#personal_newaccount>`_

With your wallet file created, you can unlock your account via web3j by first of all creating an
instance of web3j that supports Parity/Geth admin commands::

   Parity parity = Parity.build(new HttpService());

Then you can unlock the account, and providing this was successful, send a transaction::

   PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount("0x000...", "a password").sendAsync().get();
   if (personalUnlockAccount.accountUnlocked()) {
       // send a transaction
   }


Transactions for sending in this manner should be created via
`org.web3j.protocol.core.methods.request.EthSendTransaction <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/methods/request/EthSendTransaction.java>`_::

  EthSendTransaction ethSendTransaction = new EthSendTransaction(
                <to address>,
                BigInteger.valueOf(<gas price>),
                "0x...<code to execute>"
        );

        org.web3j.protocol.core.methods.response.EthSendTransaction
                transactionResponse = parity.ethSendTransaction(ethSendTransaction)
                .sendAsync().get();

        String transactionHash = transactionResponse.getTransactionHash();

        // poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash)

Please refer to the integration test
`org.web3j.protocol.scenarios <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/DeployContractIT.java>`_
and its superclass
`org.web3j.protocol.scenarios.Scenario <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/Scenario.java>`_
for further details of this transaction workflow.

Further details of working with the different admin commands supported by web3j are available in
the section :doc:`management_apis`.


.. _offline-signing:

Offline transaction signing
---------------------------

If you'd prefer not to manage your own Ethereum client, or do not want to provide wallet details
such as your password to an Ethereum client, then offline transaction signing is the way to go.

Offline transaction signing allows you to sign a transaction using your Ethereum private key from
within web3j, allowing you to have complete control over your private key. A transaction created
offline can then be sent to any Ethereum client on the network, which will propogate the
transaction out to other nodes, provided it is a valid transaction.

The downside of offline transaction signing is that you have to provide additional information in
the transaction.



.. _transfer-of-ether:

Transfer of Ether from one party to another
-------------------------------------------

The sending of Ether between two parties requires a minimal number of details of the transaction
object:

- from - the sender wallet address
- to - the destination wallet address
- value - the amount of Ether you wish to send to the destination address


.. _creation-of-smart-contract:

Creation of a smart contract
----------------------------

To deploy a new smart contract, the following attributes will need to be provided

- from - the sender wallet address
- value - the amount you wish to deposit in the smart contract (assumes zero if not provided)
- data - the hex formatted, compiled smart contract creation code


.. _transacting-with-contract:

Transacting with a smart contract
---------------------------------

To transact with an existing smart contract, the following attributes will need to be provided:

- from - the sender wallet address
- value - the amount you wish to deposit in the smart contract (assumes zero if not provided)
- data - the encoded function selector and parameter arguments

web3j takes care of the function encoding for you, further details are available in the
`Ethereum Contract ABI <https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#function-selector-and-argument-encoding>`_
section of the Ethereum Wiki.


.. _querying-state:

Querying the state of a smart contract
--------------------------------------

This functionality is facilitated by the `eth_call <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_call>`_
JSON-RPC call.

eth_call allows you to call a method on a smart contract to query a value. There is no transaction
cost associated with this function, this is because it does not change the state of any smart
contract method's called, it simply returns the value from them.

