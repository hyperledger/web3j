const path = require("path");
const fs = require("fs-extra");
const Web3 = require("web3");
const Web3Quorum = require("web3js-quorum");

// WARNING: the keys here are demo purposes ONLY. Please use a tool like EthSigner for production, rather than hard coding private keys
const { tessera, besu } = require("./keys.js");
const chainId = 1337;
// abi and bytecode generated from simplestorage.sol:
// > solcjs --bin --abi simplestorage.sol
const contractJsonPath = path.resolve(
  __dirname,
  "../",
  "contracts",
  "SimpleStorage.json"
);
const contractJson = JSON.parse(fs.readFileSync(contractJsonPath));
const contractBytecode = contractJson.evm.bytecode.object;
const contractAbi = contractJson.abi;
// initialize the default constructor with a value `47 = 0x2F`; this value is appended to the bytecode
const contractConstructorInit =
  "000000000000000000000000000000000000000000000000000000000000002F";

async function createPrivacyGroup(clientUrl, participantList) {
  const web3 = new Web3(clientUrl);
  const web3quorum = new Web3Quorum(web3, chainId);
  const contractOptions = {
    addresses: participantList,
    name: "web3js-quorum",
    description: "quickstart",
  };
  const result = await web3.priv.createPrivacyGroup(contractOptions);
  console.log(
    "Privacy group: " +
      result +
      " created between participants: " +
      participantList
  );
  return result;
}

// Besu doesn't support eth_sendTransaction so we use the eea_sendRawTransaction for things like simple value transfers, contract creation or contract invocation
async function createContract(
  clientUrl,
  privacyGroupId,
  fromPrivateKey,
  fromPublicKey
) {
  const web3 = new Web3(clientUrl);
  const web3quorum = new Web3Quorum(web3, chainId);
  const txOptions = {
    data: "0x" + contractBytecode + contractConstructorInit,
    privateKey: fromPrivateKey,
    privateFrom: fromPublicKey,
    privacyGroupId: privacyGroupId,
  };
  console.log("Creating contract...");
  // Generate and send the Raw transaction to the Besu node using the eea_sendRawTransaction JSON-RPC call
  const txHash = await web3quorum.priv.generateAndSendRawTransaction(txOptions);
  console.log("Getting contractAddress from txHash: ", txHash);
  const privateTxReceipt = await web3quorum.priv.waitForTransactionReceipt(
    txHash
  );
  console.log("Private Transaction Receipt: ", privateTxReceipt);
  return privateTxReceipt;
}

async function getValueAtAddress(
  clientUrl,
  nodeName = "node",
  address,
  contractAbi,
  fromPrivateKey,
  fromPublicKey,
  privacyGroupId
) {
  const web3 = new Web3(clientUrl);
  const web3quorum = new Web3Quorum(web3, chainId);
  const contract = new web3quorum.eth.Contract(contractAbi);
  // eslint-disable-next-line no-underscore-dangle
  const functionAbi = contract._jsonInterface.find((e) => {
    return e.name === "get";
  });
  const functionParams = {
    to: address,
    data: functionAbi.signature,
    privateKey: fromPrivateKey,
    privateFrom: fromPublicKey,
    privacyGroupId: privacyGroupId,
  };
  const transactionHash = await web3quorum.priv.generateAndSendRawTransaction(
    functionParams
  );
  // console.log(`Transaction hash: ${transactionHash}`);
  const result = await web3quorum.priv.waitForTransactionReceipt(
    transactionHash
  );
  console.log(
    "" + nodeName + " value from deployed contract is: " + result.output
  );
  return result;
}

async function setValueAtAddress(
  clientUrl,
  address,
  value,
  contractAbi,
  fromPrivateKey,
  fromPublicKey,
  privacyGroupId
) {
  const web3 = new Web3(clientUrl);
  const web3quorum = new Web3Quorum(web3, chainId);
  const contract = new web3quorum.eth.Contract(contractAbi);
  // eslint-disable-next-line no-underscore-dangle
  const functionAbi = contract._jsonInterface.find((e) => {
    return e.name === "set";
  });
  const functionArgs = web3quorum.eth.abi
    .encodeParameters(functionAbi.inputs, [value])
    .slice(2);
  const functionParams = {
    to: address,
    data: functionAbi.signature + functionArgs,
    privateKey: fromPrivateKey,
    privateFrom: fromPublicKey,
    privacyGroupId,
  };
  const transactionHash = await web3quorum.priv.generateAndSendRawTransaction(
    functionParams
  );
  console.log(`Transaction hash: ${transactionHash}`);
  const result = await web3quorum.priv.waitForTransactionReceipt(
    transactionHash
  );
  return result;
}

async function main() {
  const participantList = [
    tessera.member1.publicKey,
    tessera.member3.publicKey,
  ];
  const privacyGroupId = await createPrivacyGroup(
    besu.member1.url,
    participantList
  );
  createContract(
    besu.member1.url,
    privacyGroupId,
    besu.member1.accountPrivateKey,
    tessera.member1.publicKey,
    tessera.member3.publicKey
  )
    .then(async function (privateTxReceipt) {
      console.log("Address of transaction: ", privateTxReceipt.contractAddress);
      let newValue = 123;

      //wait for the blocks to propogate to the other nodes
      await new Promise((r) => setTimeout(r, 10000));
      console.log(
        "Use the smart contracts 'get' function to read the contract's constructor initialized value .. "
      );
      await getValueAtAddress(
        besu.member1.url,
        "Member1",
        privateTxReceipt.contractAddress,
        contractAbi,
        besu.member1.accountPrivateKey,
        tessera.member1.publicKey,
        privacyGroupId
      );
      console.log(
        `Use the smart contracts 'set' function to update that value to ${newValue} .. - from member1 to member3`
      );
      await setValueAtAddress(
        besu.member1.url,
        privateTxReceipt.contractAddress,
        newValue,
        contractAbi,
        besu.member1.accountPrivateKey,
        tessera.member1.publicKey,
        privacyGroupId
      );
      //wait for the blocks to propogate to the other nodes
      await new Promise((r) => setTimeout(r, 10000));
      console.log(
        "Verify the private transaction is private by reading the value from all three members .. "
      );
      await getValueAtAddress(
        besu.member1.url,
        "Member1",
        privateTxReceipt.contractAddress,
        contractAbi,
        besu.member1.accountPrivateKey,
        tessera.member1.publicKey,
        privacyGroupId
      ).catch(() => {
        console.log("Member1 cannot obtain value");
      });
      await getValueAtAddress(
        besu.member2.url,
        "Member2",
        privateTxReceipt.contractAddress,
        contractAbi,
        besu.member2.accountPrivateKey,
        tessera.member2.publicKey,
        privacyGroupId
      ).catch(() => {
        console.log("Member2 cannot obtain value");
      });
      await getValueAtAddress(
        besu.member3.url,
        "Member3",
        privateTxReceipt.contractAddress,
        contractAbi,
        besu.member3.accountPrivateKey,
        tessera.member3.publicKey,
        privacyGroupId
      ).catch(() => {
        console.log("Member3 cannot obtain value");
      });
    })
    .catch(console.error);
}

if (require.main === module) {
  main();
}

module.exports = exports = main;
