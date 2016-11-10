Troubleshooting
===============

I'm submitting a transaction, but it's not being mined
------------------------------------------------------
After creating and sending a transaction, you receive a transaction hash, however calling
`eth_getTransactionReceipt <https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionreceipt>`_
always returns a blank value, indicating the transaction has not been mined::

   String transactionHash = sendTransaction(...);

   // you loop through the following expecting to eventually get a receipt once the transaction
   // is mined
   EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
           web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

   if (!transactionReceipt.isPresent()) {
       // try again, ad infinitum
   }

However, you never receive a transaction receipt. Unfortunately there may not be a an error
in your Ethereum client indicating any issues with the transaction::

   I1025 18:13:32.817691 eth/api.go:1185] Tx(0xeaac9aab7f9aeab189acd8714c5a60c7424f86820884b815c4448cfcd4d9fc79) to: 0x9c98e381edc5fe1ac514935f3cc3edaa764cf004

The easiest way to see if the submission is waiting to mined is to refer to Etherscan
and search for the address the transaction was sent using https://testnet.etherscan.io/address/0x...
If the submission has been successful it should be visible in Etherscan within seconds of you
performing the transaction submission. The wait is for the mining to take place.

.. image:: /images/pending_transaction.png

If there is no sign of it then the transaction has vanished into the ether (sorry). The likely
cause of this is likely to be to do with the transaction's nonce either not being set, or
being too low. Please refer to the section :ref:`nonce` for more information.


I want to see details of the JSON-RPC requests and responses
------------------------------------------------------------

Set the following system properties in your main class::

   System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
   System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
   System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
