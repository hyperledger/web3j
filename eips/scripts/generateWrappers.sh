#!/usr/bin/env bash

set -e
set -o pipefail

targets="
../src/main/resources/eip20/solidity/ERC20.sol
../src/main/resources/eip721/solidity/ERC721.sol
"

for target in ${targets}; do
    dirName=$(dirname $target)
    fileName=$(basename $target)

    cd $dirName
    echo "Compiling Solidity file ${fileName}.sol:"
    solc --bin --abi --optimize --overwrite ${fileName}.sol -o build/
    echo "Complete"

    echo "Generating web3j bindings"
    web3j solidity generate \
        build/${fileName}.bin \
        build/${fileName}.abi \
        -p org.web3j.eips \
        -o ../src/main/java/org/web3j/eips > /dev/null
    echo "Complete"

    cd -
done
