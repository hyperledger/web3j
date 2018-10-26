#!/usr/bin/env bash

set -e
set -o pipefail

targetFileIn=(
"src/main/resources/eip20/solidity/ERC20"
"src/main/resources/eip165/solidity/ERC165"
"src/main/resources/eip721/solidity/ERC721"
"src/main/resources/eip721/solidity/ERC721Enumerable"
"src/main/resources/eip721/solidity/ERC721Metadata")

targetPackageOut=(
"org.web3j.eips.eip20.generated"
"org.web3j.eips.eip165.generated"
"org.web3j.eips.eip721.generated"
"org.web3j.eips.eip721.generated"
"org.web3j.eips.eip721.generated")

mkdir -p ../build/

for i in ${!targetFileIn[@]}; do
    solcFileIn=${targetFileIn[$i]}
    packageOut=${targetPackageOut[$i]}
    dirName=$(dirname $solcFileIn)
    fileName=$(basename $solcFileIn)

    cd ../$dirName
    eipsRootDir="../../../../../"

    echo "Compiling Solidity file ${fileName}.sol:"
    solc --abi --optimize --overwrite ${fileName}.sol -o ${eipsRootDir}/build/
    echo "Complete"

    echo "Generating web3j bindings"
    web3j solidity generate \
        -a ${eipsRootDir}build/${fileName}.abi \
        -p ${packageOut} \
        -o ${eipsRootDir}/src/main/java/ > /dev/null
    echo "Complete"

    cd -
done
