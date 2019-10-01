Web3j CLI
=========

A web3j binary is distributed with each release providing an interactive command line (CLI). It allows you to use some of the key functionality of web3j from your terminal, including:

- New project creation
- Project creation from with Solidity code
- Wallet creation
- Wallet password management
- Ether transfer from one wallet to another
- Generation of Solidity smart contract wrappers

Installation
------------

### Script

The simplest way to install the Web3j CLI is via the following script:

```bash
curl -s https://raw.githubusercontent.com/web3j/web3j-installer/master/web3j.sh | bash
```

### Manual installation

The command line tools can also be obtained as a zipfile/tarball from the [releases](https://github.com/web3j/web3j/releases/latest) page of the project repository, under the **Downloads** section, or for OS X users via [Homebrew](https://github.com/web3j/homebrew-web3j), or for Arch linux users via the [AUR](https://aur.archlinux.org/packages/web3j/).

``` bash
brew tap web3j/web3j
brew install web3j
```

To run via the zipfile, simply extract the zipfile and run the binary:

``` console
$ unzip web3j-<version>.zip
   creating: web3j-3.0.0/lib/
  inflating: web3j-3.0.0/lib/core-1.0.2-all.jar
   creating: web3j-3.0.0/bin/
  inflating: web3j-3.0.0/bin/web3j
  inflating: web3j-3.0.0/bin/web3j.bat
$ ./web3j-<version>/bin/web3j

              _      _____ _     _
             | |    |____ (_)   (_)
__      _____| |__      / /_     _   ___
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                         _/ |
                        |__/

Usage: web3j version|wallet|solidity|new|import ...
```

Web3j new &  import
-----------------------------------------
The `web3j new` and `web3j import` commands provide a convenient way to create a new java project using Web3j's Command Line Tools.

These commands provide the following functionality:

- Generate a java project.
- Generate a Greeter solidity contract or import solidity contracts from a directory.
- Compiling the solidity files.
- Build the project using gradle wrapper.
- Generate java classes for the solidity files.
- Pre-defined dependencies (web3j), to deploy and interact with the contracts.

### web3j new

To generate a new project interactively:

```bash
web3j new 
``` 

Or using non-interactive mode:

```bash
web3j new -n <project name> -p <package name> [-o <path>]
```

The `-o` option can be omitted if you want to generate the project in the current directory.

The `project name ` and `package name` values must comply with the Java standard. The project name is also used as the class name.

### web3j import

Similarly to `web3j new`, `web3j import` will create a new java project but with user defined smart contracts.

To generate a new project with your own project:

```
web3j import -n <project name> -p <package name> -s <path to solidity sources> [-o <path>]
```

or 

```
web3j import 
```

The `-s` option will work with a single solidity file or a folder containing solidity files.


Wallet tools
------------

To generate a new Ethereum wallet:

``` bash
$ web3j wallet create
```

To update the password for an existing wallet:

``` bash
$ web3j wallet update <walletfile>
```

To send Ether to another address:

``` bash
$ web3j wallet send <walletfile> 0x<address>|<ensName>
```

When sending Ether to another address you will be asked a series of questions before the transaction takes place. See the below for a full example

The following example demonstrates using web3j to send Ether to another wallet.

``` console
$ ./web3j-<version>/bin/web3j wallet send <walletfile> 0x<address>|<ensName>

              _      _____ _     _
             | |    |____ (_)   (_)
__      _____| |__      / /_     _   ___
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                         _/ |
                        |__/

Please enter your existing wallet file password:
Wallet for address 0x19e03255f667bdfd50a32722df860b1eeaf4d635 loaded
Please confirm address of running Ethereum client you wish to send the transfer request to [http://localhost:8545/]:
Connected successfully to client: Geth/v1.4.18-stable-c72f5459/darwin/go1.7.3
What amound would you like to transfer (please enter a numeric value): 0.000001
Please specify the unit (ether, wei, ...) [ether]:
Please confim that you wish to transfer 0.000001 ether (1000000000000 wei) to address 0x9c98e381edc5fe1ac514935f3cc3edaa764cf004
Please type 'yes' to proceed: yes
Commencing transfer (this may take a few minutes)...................................................................................................................$

Funds have been successfully transferred from 0x19e03255f667bdfd50a32722df860b1eeaf4d635 to 0x9c98e381edc5fe1ac514935f3cc3edaa764cf004
Transaction hash: 0xb00afc5c2bb92a76d03e17bd3a0175b80609e877cb124c02d19000d529390530
Mined block number: 1849039
```


Solidity smart contract wrapper generator
-----------------------------------------

Please refer to [Solidity smart contract wrappers](smart_contracts.md#solidity-smart-contract-wrappers).
