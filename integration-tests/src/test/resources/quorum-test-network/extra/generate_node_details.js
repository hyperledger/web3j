const secp256k1 = require('secp256k1')
const keccak = require('keccak')
const { randomBytes } = require('crypto')
const fs = require('fs')
const Wallet = require('ethereumjs-wallet');
const yargs = require('yargs/yargs');

function generatePrivateKey() {
  let privKey
  do {
    privKey = randomBytes(32)
  } while (!secp256k1.privateKeyVerify(privKey))
  return privKey
}

function derivePublicKey(privKey) {
  // slice on the end to remove the compression prefix ie. uncompressed use 04 prefix & compressed use 02 or 03
  // we generate the address, which wont work with the compression prefix
  let pubKey = secp256k1.publicKeyCreate(privKey, false).slice(1)
  return Buffer.from(pubKey)
}
  
function deriveAddress(pubKey) {
  if(!Buffer.isBuffer(pubKey)) {
    console.log("ERROR - pubKey is not a buffer")
  }
  let keyHash = keccak('keccak256').update(pubKey).digest()
  return keyHash.slice(Math.max(keyHash.length - 20, 1))
}
  
function generateNodeData() {
  let privateKey = generatePrivateKey()
  let publicKey = derivePublicKey(privateKey)
  let address = deriveAddress(publicKey)
  console.log("keys created, writing to file...")
  fs.writeFileSync("nodekey", privateKey.toString('hex'));
  fs.writeFileSync("nodekey.pub", publicKey.toString('hex'));
  fs.writeFileSync("address", address.toString('hex'));
}

async function main(password) {

  // generate nodekeys
  generateNodeData();

  // generate account
  const wallet = Wallet['default'].generate();
  const v3keystore = await wallet.toV3(password);
  console.log("account created, writing to file...")
  fs.writeFileSync("accountKeystore", JSON.stringify(v3keystore));
  fs.writeFileSync("accountPrivateKey", wallet.getPrivateKeyString());
  fs.writeFileSync("accountPassword", password);
  return {
    privateKey: wallet.getPrivateKeyString(),
    keystore: JSON.stringify(v3keystore),
    password: password
  }
}

try {
  const args = yargs(process.argv.slice(2)).options({
    password: { type: 'string', demandOption: false, default: '', describe: 'Password for the account' }
  }).argv;
  main(args.password);
} catch {
  console.error(e)
}

