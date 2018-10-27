#!/usr/bin/env bash

targets="
arrays/Arrays
contracts/HumanStandardToken
fibonacci/Fibonacci
greeter/Greeter
shipit/ShipIt
simplestorage/SimpleStorage
"

for target in ${targets}; do
    dirName=$(dirname $target)
    fileName=$(basename $target)

    cd $dirName
    echo "Compiling Solidity files in ${dirName}:"
    solc --bin --abi --optimize --overwrite ${fileName}.sol -o build/
    echo "Complete"

    echo "Generating web3j bindings"
    web3j solidity generate \
        -b build/${fileName}.bin \
        -a build/${fileName}.abi \
        -p org.web3j.generated \
        -o ../../../../../../integration-tests/src/test/java/ > /dev/null
    echo "Complete"

    cd -
done
