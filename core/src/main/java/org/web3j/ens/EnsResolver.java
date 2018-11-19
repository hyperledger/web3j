package org.web3j.ens;

import org.web3j.compat.Compat;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.contracts.generated.ENS;
import org.web3j.ens.contracts.generated.PublicResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

/**
 * Resolution logic for contract addresses.
 */
public class EnsResolver {

    static final long DEFAULT_SYNC_THRESHOLD = 1000 * 60 * 3;
    static final String REVERSE_NAME_SUFFIX = ".addr.reverse";

    private final Web3j web3j;
    private final TransactionManager transactionManager;
    private long syncThreshold;  // non-final in case this value needs to be tweaked

    public EnsResolver(Web3j web3j, long syncThreshold) {
        this.web3j = web3j;
        transactionManager = new ClientTransactionManager(web3j, null);  // don't use empty string
        this.syncThreshold = syncThreshold;
    }

    public EnsResolver(Web3j web3j) {
        this(web3j, DEFAULT_SYNC_THRESHOLD);
    }

    public void setSyncThreshold(long syncThreshold) {
        this.syncThreshold = syncThreshold;
    }

    public long getSyncThreshold() {
        return syncThreshold;
    }

    /**
     * Provides an access to a valid public resolver in order to access other API methods.
     * @param ensName our user input ENS name
     * @return PublicResolver
     */
    public PublicResolver obtainPublicResolver(String ensName) {
        if (isValidEnsName(ensName)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupResolver(ensName);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }

        } else {
            throw new EnsResolutionException("EnsName is invalid: " + ensName);
        }
    }

    public String resolve(String contractId) {
        if (isValidEnsName(contractId)) {
            PublicResolver resolver = obtainPublicResolver(contractId);

            byte[] nameHash = NameHash.nameHashAsBytes(contractId);
            String contractAddress = null;
            try {
                contractAddress = resolver.addr(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute Ethereum request", e);
            }

            if (!WalletUtils.isValidAddress(contractAddress)) {
                throw new RuntimeException("Unable to resolve address for name: " + contractId);
            } else {
                return contractAddress;
            }
        } else {
            return contractId;
        }
    }

    /**
     * Reverse name resolution as documented in the
     * <a href="https://docs.ens.domains/en/latest/userguide.html#reverse-name-resolution">specification</a>.
     * @param address an ethereum address, example: "0x314159265dd8dbb310642f98f50c066173c1259b"
     * @return a EnsName registered for provided address
     */
    public String reverseResolve(String address) {
        if (WalletUtils.isValidAddress(address)) {
            String reverseName = Numeric.cleanHexPrefix(address) + REVERSE_NAME_SUFFIX;
            PublicResolver resolver = obtainPublicResolver(reverseName);

            byte[] nameHash = NameHash.nameHashAsBytes(reverseName);
            String name = null;
            try {
                name = resolver.name(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute Ethereum request", e);
            }

            if (!isValidEnsName(name)) {
                throw new RuntimeException("Unable to resolve name for address: " + address);
            } else {
                return name;
            }
        } else {
            throw new EnsResolutionException("Address is invalid: " + address);
        }
    }

    PublicResolver lookupResolver(String ensName) throws Exception {
        NetVersion netVersion = web3j.netVersion().send();
        String registryContract = Contracts.resolveRegistryContract(netVersion.getNetVersion());

        ENS ensRegistry = ENS.load(
                registryContract, web3j, transactionManager,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);

        byte[] nameHash = NameHash.nameHashAsBytes(ensName);

        String resolverAddress = ensRegistry.resolver(nameHash).send();
        PublicResolver resolver = PublicResolver.load(
                resolverAddress, web3j, transactionManager,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);

        return resolver;
    }

    boolean isSynced() throws Exception {
        EthSyncing ethSyncing = web3j.ethSyncing().send();
        if (ethSyncing.isSyncing()) {
            return false;
        } else {
            EthBlock ethBlock =
                    web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            long timestamp = Compat.longValueExact(ethBlock.getBlock().getTimestamp()) * 1000;

            return System.currentTimeMillis() - syncThreshold < timestamp;
        }
    }

    public static boolean isValidEnsName(String input) {
        return input != null  // will be set to null on new Contract creation
                && (input.contains(".") || !WalletUtils.isValidAddress(input));
    }
}
