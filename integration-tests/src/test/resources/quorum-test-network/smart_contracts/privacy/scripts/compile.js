const path = require('path');
const fs = require('fs-extra');
const solc = require('solc');

const contractsPath = path.resolve(__dirname, '../', 'contracts');

function buildSources() {
  const sources = {};
  const contractsFiles = fs.readdirSync(contractsPath);
  contractsFiles.forEach(file => {
    if(file.endsWith(".sol")){
      const contractFullPath = path.resolve(contractsPath, file);
      sources[file] = {
        content: fs.readFileSync(contractFullPath, 'utf8')
      };
    }
  });
  return sources;
}

const input = {
	language: 'Solidity',
	sources: buildSources(),
	settings: {
		outputSelection: {
			'*': {
				'*': [ '*', 'evm.bytecode'  ]
			}
		}
	}
}

function compileContracts() {
  const stringifiedJson = JSON.stringify(input);
  const compilationResult = solc.compile(stringifiedJson);
  const output = JSON.parse(compilationResult);
	const compiledContracts = output.contracts;
	for (let contract in compiledContracts) {
		for(let contractName in compiledContracts[contract]) {
			fs.outputJsonSync(
				path.resolve(contractsPath, `${contractName}.json`),
				compiledContracts[contract][contractName], { spaces: 2 }
			)
		}
	}
}

const main = () => {
	compileContracts();
}

if (require.main === module) {
  main();
}

module.exports = exports = main


