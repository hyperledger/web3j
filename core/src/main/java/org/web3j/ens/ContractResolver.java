package org.web3j.ens;

import org.web3j.crypto.WalletUtils;
import org.web3j.ens.contracts.generated.ENS;
import org.web3j.ens.contracts.generated.PublicResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

/**
 * Resolution logic for contract addresses.
 */
public class ContractResolver {

    static final long DEFAULT_SYNC_THRESHOLD = 1000 * 60 * 3;

    private final Web3j web3j;
    private final TransactionManager transactionManager;
    private final long syncThreshold;

    public ContractResolver(Web3j web3j, long syncThreshold) {
        this.web3j = web3j;
        transactionManager = new ClientTransactionManager(web3j, null);  // don't use empty string
        this.syncThreshold = syncThreshold;
    }

    public ContractResolver(Web3j web3j) {
        this(web3j, DEFAULT_SYNC_THRESHOLD);
    }

    public String resolve(String contractId) {
        if (isValidEnsName(contractId)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupAddress(contractId);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }

        } else {
            return contractId;
        }
    }

    String lookupAddress(String ensName) throws Exception {
        NetVersion netVersion = web3j.netVersion().send();
        String registryContract = Contracts.resolveRegistryContract(netVersion.getNetVersion());

        ENS ensRegistry = ENS.load(
                registryContract, web3j, transactionManager,
                ManagedTransaction.GAS_PRICE, org.web3j.tx.Contract.GAS_LIMIT);

        byte[] nameHash = Numeric.hexStringToByteArray(NameHash.nameHash(ensName));

        String resolverAddress = ensRegistry.resolver(nameHash).send();
        PublicResolver resolver = PublicResolver.load(
                resolverAddress, web3j, transactionManager,
                ManagedTransaction.GAS_PRICE, org.web3j.tx.Contract.GAS_LIMIT);

        String contractAddress = resolver.addr(nameHash).send();

        if (!WalletUtils.isValidAddress(contractAddress)) {
            throw new RuntimeException("Unable to resolve address for name: " + ensName);
        } else {
            return contractAddress;
        }
    }

    boolean isSynced() throws Exception {
        EthSyncing ethSyncing = web3j.ethSyncing().send();
        if (ethSyncing.isSyncing()) {
            return false;
        } else {
            EthBlock ethBlock =
                    web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            long timestamp = ethBlock.getBlock().getTimestamp().longValueExact() * 1000;

            return System.currentTimeMillis() - syncThreshold < timestamp;
        }
    }

    public static boolean isValidEnsName(String input) {
        return input != null
                && (input.contains(".") || !WalletUtils.isValidAddress(input));
    }
}
