#!/usr/bin/env bash

set -e
set -o pipefail

targets="
ens/ENS
ens/PublicResolver
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
        -b build/${fileName}.bin \
        -a build/${fileName}.abi \
        -p org.web3j.ens.contracts.generated \
        -o ../../../../main/java/ > /dev/null
    echo "Complete"

    cd -
done
