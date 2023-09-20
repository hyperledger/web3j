const Web3 = require('web3')

// Web3 initialization (should point to the JSON-RPC endpoint)
const web3 = new Web3(new Web3.providers.HttpProvider('http://127.0.0.1:8545'))

var V3KeyStore = web3.eth.accounts.encrypt("797bbe0373132e8c5483515b68ecbb6d3581b56f0205b653ad2b30a559e83891", "Password1");
console.log(JSON.stringify(V3KeyStore));
process.exit();
