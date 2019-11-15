Web3j CLI
=========

A web3j binary is distributed with each release providing an interactive command line (CLI). It allows you to use some of the key functionality of web3j from your terminal, including:

- New project creation
- Project creation from existing Solidity code
- Wallet creation
- Wallet password management
- Ether transfer from one wallet to another
- Generation of Solidity smart contract wrappers
- Smart contract auditing

Installation
------------

### Script

The simplest way to install the Web3j CLI is via the following script:

```bash
curl -L https://get.web3j.io | sh
```

You can veriify the installation was successful by running th web3j command:

```bash
web3j version

              _      _____ _     _        
             | |    |____ (_)   (_)       
__      _____| |__      / /_     _   ___  
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \ 
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/ 
                         _/ |             
                        |__/              

Version: 4.5.5
Build timestamp: 2019-09-24 16:00:04.332 UTC
```

If you encounter problems with the installation, please refer to the manual installation instructions [below](#manual-installation).


Web3j new & import
-----------------------------------------
The `web3j new` and `web3j import` commands provide a convenient way to create a new Java project using Web3j's Command Line Tools.

These commands provide the following functionality:

- Creation of a new Java project.
- Generation of a simple *Hello World* Solidity contract (named the `Greeter`) or import an exisiting Solidity project from a file or directory.
- Compilation of the Solidity files.
- Configure the project to use the Gradle build tool.
- Generate Java smart contract wrappers for all provided Solidity files.
- Add the required web3j dependencies, to deploy and interact with the contracts.

### web3j new

To generate a new project interactively:

```bash
web3j new 
``` 

You will be prompted to answer a series of questions to create your project:

```bash
$ web3j new

              _      _____ _     _        
             | |    |____ (_)   (_)       
__      _____| |__      / /_     _   ___  
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \ 
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/ 
                         _/ |             
                        |__/              

Please enter the project name (required): 
MyProject
Please enter the package name for your project (required): 
com.web3labs.eth
Please enter the destination of your project (default .): 
myproject
Downloading https://services.gradle.org/distributions/gradle-5.0-bin.zip
...................................................................
Done
Project MyProject created at location: myproject
..............

Welcome to Gradle 5.0!

Here are the highlights of this release:
 - Kotlin DSL 1.0
 - Task timeouts
 - Dependency alignment aka BOM support
 - Interactive `gradle init`

For more details see https://docs.gradle.org/5.0/release-notes.html

$
```

Details of the created project structure are [below](#generated-project-structure).


Or using non-interactive mode:

```bash
web3j new -n <project name> -p <package name> [-o <path>]
```

The `-o` option can be omitted if you want to generate the project in the current directory.

The `project name ` and `package name` values must comply with the Java standard. The project name is also used as the class name.



### web3j import

Similarly to `web3j new`, `web3j import` will create a new java project but with user defined smart contracts.

Again, to generate a new project interactively:

```bash
web3j new 
``` 

You will be prompted to answer a series of questions to create your project:

```bash
$ web3j import

              _      _____ _     _        
             | |    |____ (_)   (_)       
__      _____| |__      / /_     _   ___  
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \ 
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/ 
                         _/ |             
                        |__/              

Please enter the project name (Required Field): 
MyImportedProject
Please enter the package name for your project (Required Field): 
com.web3labs.eth
Please enter the path to your solidity file/folder (Required Field): 
/path/to/solidity
Please enter the destination of your project (current by default): 
.
Project created with name: myimportedproject at location: .
$
```

This command can also be used non-interactively

```
web3j import -n <project name> -p <package name> -s <path to solidity sources> [-o <path>]
```

or 

```
web3j import 
```

The `-s` option will work with a single solidity file or a folder containing solidity files.


Generated project structure
---------------------------

Your application code and tests will be located in the following project directories:

- `./src/main/java` - Generated Java appliciation code stub
- `./src/test/java` - Generated Java test code stub
- `./src/main/solidity` - Solidity source code

If you need to edit the build file, it is located in the project root directory:

- `./build.gradle` - Gradle build configuration file

Additionally there are the following Gradle artifacts which you can ignore.

- `/gradle` - local Gradle installation
- `/.gradle` - local Gradle cache
- `/build` - compiled classes including smart contract bindings

If you need to view any of the generated Solidity or contract artifacts, they are available in the following locations.

Solidity `bin` and `abi` files are located at:

- `./build/resources/main/solidity/`

The source code for the generated smart contract bindings can be found at:

- `./build/generated/source/web3j/main/java/<your-package>/generated/contracts`

The compiled code for the generated smart contracts bindings is available at the below location. These are the artifacts that you use to deploy, transact and query your smart contracts.

- `./build/classes/java/main/<your-package>/generated/contracts/`


Build commands
--------------

Web3j projects use the Gradle build tool under the covers. Gradle is a build DSL  for JVM projects used widely in Java, Kotlin and Android projects. You shouldn't need to be too concerned with the semantics of Gradle beyond the following build commands:

To build the project run:

```bash
./gradlew build
```

To update the just the smart contract bindings following changes to the Solidity code run:

```bash
./gradlew generateContractWrappers
```

To delete all project build artifacts, creating a clean environment, run:

```bash
./gradlew clean
```



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

Auditing Tools
------------

Smart contracts written in Solidity often include logic bugs and other issues which might compromise their security. These are not always obvious to programmers. Issues can include [integer precision problems](https://github.com/smartdec/classification#arithmetic), [re-entrancy attacks](https://github.com/smartdec/classification#contract-interaction), and many other flaws. As Ethereum smart contracts are immutable once they have been deployed, it is important that they are bug-free at this point. Web3j is able to audit smart contracts for certain common issues and vulnerabilities using static code analysis. 

To audit a smart contract (in this instance, Campaign.sol):

``` bash
$ web3j audit Campaign.sol
```

An example of the output from this command is as follows:
``` bash
              _      _____ _     _        
             | |    |____ (_)   (_)       
__      _____| |__      / /_     _   ___  
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \ 
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/ 
                         _/ |             
                        |__/              

./Campaign.sol
   131:58   severity:2   Multiplication after division                  SOLIDITY_DIV_MUL_09hhh1                              
   91:8     severity:1   Revert inside the if-operator                  SOLIDITY_REVERT_REQUIRE_c56b12                       
   5:4      severity:1   Use of SafeMath                                SOLIDITY_SAFEMATH_837cac                             
   148:4    severity:1   Replace multiple return values with a struct   SOLIDITY_SHOULD_RETURN_STRUCT_83hf3l                 
   125:4    severity:1   Prefer external to public visibility level     SOLIDITY_UNUSED_FUNCTION_SHOULD_BE_EXTERNAL_73ufc1   

✖ 5 problems (5 errors)

```

The output is in the form of a list of issues/errors detected by the static analysis tool. The first column of output shows the line and the character at which the issue was encountered. The second column shows the severity; this ranges from 1 to 3, with 3 being the most severe. The next column contains a description of the issue found, and the final column provides a reference to the rule used to find the issue.


This functionality is provided by [SmartCheck](https://github.com/smartdec/smartcheck).


Solidity smart contract wrapper generator
-----------------------------------------

Please refer to [Solidity smart contract wrappers](smart_contracts.md#solidity-smart-contract-wrappers).


Manual installation
-------------------
Manual installation

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
