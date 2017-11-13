#!/usr/bin/env bash

targets="
MetaCoin/MetaCoin
"

for target in ${targets}; do
    dirName=$(dirname $target)
    fileName=$(basename $target)

    cd $dirName
    echo "Generating web3j bindings"
    web3j truffle generate \
        build/contracts/${fileName}.json \
        -p org.web3j.generated \
        -o ../../../../../../integration-tests/src/test/java/ > /dev/null
    echo "Complete"

    cd -
done
