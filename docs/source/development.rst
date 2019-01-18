Developer Guide
===============

Dependency management
---------------------

We recommend you use formal releases of web3j, these can be found on most public maven `repositories <https://mvnrepository.com/search?q=web3j>`_.

| Release versions follow the ``<major>.<minor>.<build>`` convention, for example: 4.1.0

| Snapshot versions of web3j follow the ``<major>.<minor>.<build>-SNAPSHOT`` convention, for example: 4.1.0-SNAPSHOT.

| If you would like to use snapshots instead please add a new maven repository pointing to:

::

  https://oss.sonatype.org/content/repositories/snapshots

Please refer to the `maven <https://maven.apache.org/guides/mini/guide-multiple-repositories.html>`_ or `gradle <https://maven.apache.org/guides/mini/guide-multiple-repositories.html>`_ documentation for further detail.

Sample gradle configuration:

.. code-block:: groovy

   repositories {
      maven {
         url "https://oss.sonatype.org/content/repositories/snapshots"
      }
   }

Sample maven configuration:

.. code-block:: xml

   <repositories>
     <repository>
       <id>sonatype-snasphots</id>
       <name>Sonatype snapshots repo</name>
       <url>https://oss.sonatype.org/content/repositories/snapshots</url>
     </repository>
   </repositories>

Building web3j
--------------

web3j includes integration tests for running against a live Ethereum client. If you do not have a
client running, you can exclude their execution as per the below instructions.

To run a full build (excluding integration tests):

.. code-block:: bash

   $ ./gradlew check


To run the integration tests:

.. code-block:: bash

   $ ./gradlew  -Pintegration-tests=true :integration-tests:test


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
