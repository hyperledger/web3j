Filters and Events
==================

Filters provide notifications of certain events taking place in the Ethereum network. There are
three classes of filter supported in Ethereum:

#. Block filters
#. Pending transaction filters
#. Topic filters

Block filters and pending transaction filters provide notification of the creation of new
transactions or blocks on the network.

Topic filters are more flexible. These allow you to create a filter based on specific criteria
that you provide.

Unfortunately, unless you are using a WebSocket connection to Geth, working with filters via the
JSON-RPC API is a tedious process, where you need to poll the Ethereum client in order to find out
if there are any updates to your filters due to the synchronous nature of HTTP and IPC requests.
Additionally the block and transaction filters only provide the transaction or block hash, so a
further request is required to obtain the actual transaction or block referred to by the hash.

web3j's managed `Filter <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/filters/Filter.java>`_
implementation address these issues, so you have a fully asynchronous event based API for working
with filters. It uses `RxJava <https://github.com/ReactiveX/RxJava>`_'s
`Observables <http://reactivex.io/documentation/observable.html>`_ which provides a consistent API
for working with events, which facilitates the chaining together of JSON-RPC calls via
functional composition.

**Note:** filters are not supported on Infura.


Block and transaction filters
-----------------------------

To receive all new blocks as they are added to the blockchain (the false parameter specifies that
we only want the blocks, not the embedded transactions too)::

   Subscription subscription = web3j.blockObservable(false).subscribe(block -> {
       ...
   });

To receive all new transactions as they are added to the blockchain::

   Subscription subscription = web3j.transactionObservable().subscribe(tx -> {
       ...
   });

To receive all pending transactions as they are submitted to the network (i.e. before they have
been grouped into a block together)::

   Subscription subscription = web3j.pendingTransactionObservable().subscribe(tx -> {
       ...
   });

Subscriptions should always be cancelled when no longer required via *unsubscribe*::

   subscription.unsubscribe();

Other callbacks are also provided which provide simply the block or transaction hashes,
for details of these refer to the
`Web3jRx <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/rx/Web3jRx.java>`_
interface.


Topic filters and EVM events
----------------------------

Topic filters capture details of Ethereum Virtual Machine (EVM) events taking place in the network.
These events are created by smart contracts and stored in the transaction log associated with a
smart contract.

The `Solidity documentation <http://solidity.readthedocs.io/en/develop/contracts.html?highlight=event#events>`_
provides a good overview of EVM events.

You use the
`EthFilter <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/core/methods/request/EthFilter.java>`_
type to specify the topics that you wish to apply to the filter. This can include the address of
the smart contract you wish to apply the filter to. You can also provide specific topics to filter
on. Where the individual topics represent indexed parameters on the smart contract::

   EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
           DefaultBlockParameterName.LATEST, <contract-address>)
                [.addSingleTopic(...) | .addOptionalTopics(..., ...) | ...];

This filter can then be created using a similar syntax to the block and transaction filters above::

   web3j.ethLogObservable(filter).subscribe(log -> {
       ...
   });

The filter topics can only refer to the indexed Solidity event parameters. It is not possible to
filter on the non-indexed event parameters. Additionally, for any indexed event parameters that are
variable length array types such as string and bytes, the Keccak-256 hash of their value is stored
on the EVM log. It is not possible to store or filter using their full value.

If you create a filter instance with no topics associated with it, all EVM events taking place in
the network will be captured by the filter.


A note on functional composition
--------------------------------

In addition to *send()* and *sendAsync*, all JSON-RPC method implementations in web3j support the
*observable()* method to create an Observable to execute the request asynchronously. This makes it
very straight forwards to compose JSON-RPC calls together into new functions.

For instance, the
`blockObservable <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/rx/JsonRpc2_0Rx.java>`_ is
itself composed of a number of separate JSON-RPC calls::

   public Observable<EthBlock> blockObservable(
           boolean fullTransactionObjects, long pollingInterval) {
       return this.ethBlockHashObservable(pollingInterval)
               .flatMap(blockHash ->
                       web3j.ethGetBlockByHash(blockHash, fullTransactionObjects).observable());
   }

Here we first create an observable that provides notifications of the block hash of each newly
created block. We then use *flatMap* to invoke a call to *ethGetBlockByHash* to obtain the full
block details which is what is passed to the subscriber of the observable.


Further examples
----------------

Please refer to the integration test
`ObservableIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/core/ObservableIT.java>`_
for further examples.

For a demonstration of using the manual filter API, you can take a look at the test
`EventFilterIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/scenarios/EventFilterIT.java>`_..
