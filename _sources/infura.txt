Using Infura with web3j
=======================

Signing up
----------

The `Infura <https://infura.io/>`_ service by `Consensys <https://consensys.net/>`_, provides
Ethereum clients running in the cloud, so you don't have to run one yourself to work with Ethereum.

When you sign up to the service you are provided with a token you can use to connect to the
relevant Ethereum network:

Main Ethereum Network:
  https://mainnet.infura.io/your-token

Test Ethereum Network (Morden):
  https://morden.infura.io/your-token

If you need some Ether on testnet to get started, please post a message with your wallet address
to the `web3j Gitter channel <https://gitter.im/web3j/web3j>`_ and I'll send you some.

InfuraHttpClient
----------------

web3j comes with an Infura HTTP client
(`InfuraHttpService <https://github.com/web3j/web3j/blob/master/src/main/java/org/web3j/protocol/infura/InfuraHttpService.java>`_)
so you don't need to concern yourself with installing an HTTP certificate into your JVM. Upon
startup web3j will connect to the Infura endpoint you have specified and downloads the Infura
node TLS certificate to a temporary local key store which exists for the duration of your
application runtime::

   Web3j web3 = Web3j.build(new InfuraHttpService("https://morden.infura.io/your-token"));

Alternatively, if you'd rather install the key permanently in your JVM keystore, see
:ref:`below <certificate-installation>`.


.. _certificate-installation:

Certificate installation
------------------------

As the Infura nodes are accessed over TLS, you will need to install the relevant certificates into
your Java Virtual Machine's keystore.

You can obtain the certificate using the `Firefox <https://www.mozilla.org/en-US/firefox/new/>`_
web browser:

.. image:: /images/infura_cert.png

Alternatively, you can use SSL (this will only download the first certificate in the chain, which
should be adequate).

.. code-block:: bash

   $ openssl s_client -connect morden.infura.io:443 -showcerts </dev/null 2>/dev/null|openssl x509 -outform PEM > infura-morden.pem

Once you have downloaded it, then install it into your keystore (for Windows/Linux hosts the paths
will differ slightly):

.. code-block:: bash

   $ $JAVA_HOME/Contents/Home/jre/bin/keytool -import -noprompt -trustcacerts -alias morden.infura.io -file  ~/Downloads/morden.infura.io -keystore $JAVA_HOME/Contents/Home/jre/lib/security/cacerts -storepass changeit


Connecting
----------

You can now use Infura to work with the Ethereum blockchain.

::

   Web3j web3 = Web3j.build(new HttpService("https://morden.infura.io/your-token"));
   Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
   System.out.println(web3ClientVersion.getWeb3ClientVersion());

.. code-block:: bash

   Parity//v1.3.5-beta/x86_64-linux-gnu/rustc1.12.0


If you want to test a number of the JSON-RPC calls against Infura, update the integration test
`CoreIT <https://github.com/web3j/web3j/blob/master/src/integration-test/java/org/web3j/protocol/core/CoreIT.java>`_
with your Infura URL & run it.


Transactions
------------

In order to transact with Infura nodes, you will need to create and sign transactions offline
before sending them, as Infura nodes have no visibility of your encrypted Ethereum key files, which
are required to unlock accounts via the Personal Geth/Parity admin commands.

Refer to the :ref:`offline-signing` and :doc:`management_apis` sections for further details.