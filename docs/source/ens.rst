Ethereum Name Service
=====================

The `Ethereum Name Service (ENS) <https://ens.domains>`_ provides a human readable names to
identify addresses on the Ethereum network. It is similar to the internet's domain name service
(DNS) which provides human-readable domain names which are mapped to IP addresses.

In the case of ENS, the addresses are either wallet or smart contract addresses.

E.g. instead of using the wallet address *0x19e03255f667bdfd50a32722df860b1eeaf4d635*, you can
use *web3j.eth*.


Usage in web3j
--------------

You can use ENS names anywhere you wish to transact in web3j. In practice this means, in smart
contract wrappers, when you load them, such as::

   YourSmartContract contract = YourSmartContract.load(
           "0x<address>|<ensName>", web3j, credentials, GAS_PRICE, GAS_LIMIT);

Also, when performing Ether transfers, such as using the command line tools:

.. code-block:: bash

   $ web3j wallet send <walletfile> 0x<address>|<ensName>


.. _ens-implementation:

web3j implementation
--------------------

Behind the scenes, whenever you using web3j's transaction managers (which are derived from the
`ManagedTransaction <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/ManagedTransaction.java>`_
class), the `EnsResolver <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/ens/EnsResolver.java>`_
is invoked to perform an ENS lookup if applicable.

The resolution process is as follows:

- Check to see if our Ethereum node is fully synced
- If not fail
- If it is synced, check the timestamp on the most recent block it has.
    - If it's more than 3 minutes old, fail.
    - Otherwise perform the lookup

If you need to change the threshold parameter of what constitutes being synced to something other
then 3 minutes, this can be done via the *setSyncThreshold* method in the
`ManagedTransaction <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/ManagedTransaction.java>`_
class.


Unicode Technical Standard (UTS) #46
------------------------------------

`UTS #46 <unicode.org/reports/tr46/>`_ is the standard used to sanitise input on domain names.
The web3j ENS implementation peforms this santisation on all inputs before attempting resolution.
For details of the implementation, refer to the
`NameHash <https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/ens/NameHash.java>`_ class.


Registering domain names
------------------------

Currently, web3j only supports the resolution of ENS domains. It does not support the registration.
For instructions on how to do this, refer to the ENS
`quickstart <http://docs.ens.domains/en/latest/quickstart.html>`_.
