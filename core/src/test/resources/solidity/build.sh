#!/usr/bin/env bash

targets="
arrays/Arrays
contracts/HumanStandardToken
fibonacci/Fibonacci
greeter/Greeter
shipit/ShipIt
simplestorage/SimpleStorage
"

for target in $targets; do
    dirName=$(dirname $target)
    fileName=$(basename $target)

    cd $dirName
    echo "Compiling Solidity files"
    solc --bin --abi --optimize --overwrite ${fileName}.sol -o build/
    echo "Complete"

    echo "Generating web3j bindings"
    web3j solidity generate \
        build/${fileName}.bin \
        build/${fileName}.abi \
        -p org.web3j.generated \
        -o ../../../../integration-test/java/ > /dev/null
    echo "Complete"

    cd -
done
