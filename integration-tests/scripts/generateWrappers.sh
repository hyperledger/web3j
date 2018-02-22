#!/usr/bin/env bash

targets="
arrays/build/Arrays
contracts/build/HumanStandardToken
fibonacci/build/Fibonacci
greeter/build/Greeter
shipit/build/ShipIt
simplestorage/build/SimpleStorage
"

for target in ${targets}; do

    sh /Users/jaspervdbijl/Software/Blockchain/Web3j/fork/web3j/console/build/distributions/web3j-3.1.1/bin/web3j solidity generate \
        ../../codegen/src/test/resources/solidity/${target}.bin \
        ../../codegen/src/test/resources/solidity/${target}.abi \
        -o /Users/jaspervdbijl/Software/Blockchain/Web3j/fork/web3j/integration-tests/src/test/java \
        -p org.web3j.generated

done