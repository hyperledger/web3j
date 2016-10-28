Developer Guide
===============

Building web3j
--------------

web3j includes integration tests for running against a live Ethereum client. If you do not have a client running, you can exclude their execution as per the below instructions.

To run a full build including integration tests:

.. code-block:: bash

   $ ./gradlew check


To run excluding integration tests:

.. code-block:: bash

   $ ./gradlew -x integrationTest check


Generating documentation
------------------------

web3j uses the `Sphinx <http://www.sphinx-doc.org/en/stable/>`_ documentation generator.

All documentation (apart from the project README.md) resides under the
`/docs <https://github.com/web3j/web3j/tree/master/docs>`_ directory.

To build a copy of the documentation, from the project root:

.. code-block:: bash

   $ cd docs
   $ make clean html

Then browse the build documentation via:

.. code-block:: bash

   $ open build/html/index.html
