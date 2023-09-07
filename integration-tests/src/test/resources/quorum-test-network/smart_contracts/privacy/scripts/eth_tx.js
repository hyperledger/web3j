const path = require('path');
const fs = require('fs-extra');
const Web3 = require('web3');

// member1 details
const { tessera, besu, accounts } = require("./keys.js");
const host = besu.member1.url;

async function main(){
  const web3 = new Web3(host);
  //pre seeded account - test account only
  const privateKeyA = accounts['0x627306090abaB3A6e1400e9345bC60c78a8BEf57'].privateKey; 
  const accountA = web3.eth.accounts.privateKeyToAccount(privateKeyA);
  var accountABalance = web3.utils.fromWei(await web3.eth.getBalance(accountA.address));
  console.log("Account A has balance of: " + accountABalance);

  // create a new account to use to transfer eth to
  var accountB = web3.eth.accounts.create();
  var accountBBalance = web3.utils.fromWei(await web3.eth.getBalance(accountB.address));
  console.log("Account B has balance of: " + accountBBalance);

  // send some eth from A to B
  const txn = {
    nonce: web3.utils.numberToHex(await web3.eth.getTransactionCount(accountA.address)),
    from: accountA.address,
    to: accountB.address, 
    value: "0x100",  //amount of eth to transfer
    gasPrice: "0x0", //ETH per unit of gas
    gasLimit: "0x24A22" //max number of gas units the tx is allowed to use
  };

  console.log("create and sign the txn")
  const signedTx = await web3.eth.accounts.signTransaction(txn, accountA.privateKey);
  console.log("sending the txn")
  const txReceipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);
  console.log("tx transactionHash: " + txReceipt.transactionHash);

  //After the transaction there should be some ETH transferred
  accountABalance = web3.utils.fromWei(await web3.eth.getBalance(accountA.address));
  console.log("Account A has an updated balance of: " + accountABalance);
  accountBBalance = web3.utils.fromWei(await web3.eth.getBalance(accountB.address));
  console.log("Account B has an updated balance of: " + accountBBalance);

}

if (require.main === module) {
  main();
}

module.exports = exports = main

