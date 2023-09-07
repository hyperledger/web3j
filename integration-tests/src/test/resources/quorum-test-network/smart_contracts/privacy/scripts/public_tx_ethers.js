const path = require('path');
const fs = require('fs-extra');
var ethers = require('ethers');

// member1 details
const { tessera, besu } = require("./keys.js");
const host = besu.member1.url;
const accountAddress = besu.member1.accountAddress;

// abi and bytecode generated from simplestorage.sol:
// > solcjs --bin --abi simplestorage.sol
const contractJsonPath = path.resolve(__dirname, '../','contracts','SimpleStorage.json');
const contractJson = JSON.parse(fs.readFileSync(contractJsonPath));
const contractAbi = contractJson.abi;
const contractBytecode = contractJson.evm.bytecode.object

async function getValueAtAddress(provider, deployedContractAbi, deployedContractAddress){
  const contract = new ethers.Contract(deployedContractAddress, deployedContractAbi, provider);
  const res = await contract.get();
  console.log("Obtained value at deployed contract is: "+ res);
  return res
}

// You need to use the accountAddress details provided to Quorum to send/interact with contracts
async function setValueAtAddress(provider, wallet, deployedContractAbi, deployedContractAddress, value){
  const contract = new ethers.Contract(deployedContractAddress, deployedContractAbi, provider);
  const contractWithSigner = contract.connect(wallet);
  const tx = await contractWithSigner.set(value);
  // verify the updated value
  await tx.wait();
  // const res = await contract.get();
  // console.log("Obtained value at deployed contract is: "+ res);
  return tx;
}

async function createContract(provider, wallet, contractAbi, contractByteCode, contractInit) {
  const factory = new ethers.ContractFactory(contractAbi, contractByteCode, wallet);
  const contract = await factory.deploy(contractInit);
  // The contract is NOT deployed yet; we must wait until it is mined
  const deployed = await contract.deployTransaction.wait()
  //The contract is deployed now
  return contract
};

async function main(){
  const provider = new ethers.providers.JsonRpcProvider(host);
  const randomPrivateKey = await ethers.Wallet.createRandom();
  const wallet = new ethers.Wallet(randomPrivateKey, provider);

  createContract(provider, wallet, contractAbi, contractBytecode, 47)
  .then(async function(contract){
    console.log("Contract deployed at address: " + contract.address);
    console.log("Use the smart contracts 'get' function to read the contract's constructor initialized value .. " )
    await getValueAtAddress(provider, contractAbi, contract.address);
    console.log("Use the smart contracts 'set' function to update that value to 123 .. " );
    await setValueAtAddress(provider, wallet, contractAbi, contract.address, 123 );
    console.log("Verify the updated value that was set .. " )
    await getValueAtAddress(provider, contractAbi, contract.address);
    // await getAllPastEvents(host, contractAbi, tx.contractAddress);
  })
  .catch(console.error);

}

if (require.main === module) {
  main();
}

module.exports = exports = main

