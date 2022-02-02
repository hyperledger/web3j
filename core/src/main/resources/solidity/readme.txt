Contracts source: https://github.com/ensdomains/ens-contracts


PublicResolver includes the following profiles that implements different EIPs.
- ABIResolver = EIP 205 - ABI support (`ABI()`).
- AddrResolver = EIP 137 - Contract address interface. EIP 2304 - Multicoin support (`addr()`).
- ContentHashResolver = EIP 1577 - Content hash support (`contenthash()`).
- InterfaceResolver = EIP 165 - Interface Detection (`supportsInterface()`).
- NameResolver = EIP 181 - Reverse resolution (`name()`).
- PubkeyResolver = EIP 619 - SECP256k1 public keys (`pubkey()`).
- TextResolver = EIP 634 - Text records (`text()`).
- DNSResolver = Experimental support is available for hosting DNS domains on the Ethereum blockchain via ENS. [The more detail](https://veox-ens.readthedocs.io/en/latest/dns.html) is on the old ENS doc.




Generate abi file:
solc --bin --abi --optimize --overwrite ${fileName}.sol -o build/

Generate a wrapper class:
web3j generate solidity -a ${fileName}.abi -p org.web3j.ens.contracts.generated \-o ../../../../../main/java/ > /dev/null