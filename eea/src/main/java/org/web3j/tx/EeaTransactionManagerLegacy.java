package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.eea.Eea;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PrivateTransactionManager implementation for using a Pantheon node to transact.
 */
public class EeaTransactionManagerLegacy extends PrivateTransactionManager {

    private final List<String> privateFor;
    private final String privacyGroupId;

    public EeaTransactionManagerLegacy(
            final Eea eea,
            final Credentials credentials,
            final long chainId,
            final String privateFrom,
            final List<String> privateFor,
            final int attempts,
            final int sleepDuration) {
        super(eea, credentials, chainId, privateFrom, attempts, sleepDuration);
        this.privateFor = privateFor;
        this.privacyGroupId = generatePrivacyGroupId(privateFrom, privateFor);
    }

    public EeaTransactionManagerLegacy(
            final Eea eea, final Credentials credentials, final long chainId,
            final String privateFrom, final List<String> privateFor) {
        this(eea, credentials, chainId, privateFrom, privateFor,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, 15 * 1000);
    }

    public String getPrivacyGroupId() {
        return privacyGroupId;
    }

    @Override
    Object privacyGroupIdOrPrivateFor() {
        return privateFor;
    }

    private String generatePrivacyGroupId(final String privateFrom, final List<String> privateFor) {
        final List<byte[]> stringList = new ArrayList<>();
        stringList.add(Base64.getDecoder().decode(privateFrom));
        privateFor.forEach(item -> stringList.add(Base64.getDecoder().decode(item)));

        final List<RlpType> rlpList = stringList.stream().distinct()
                .sorted(Comparator.comparing(Arrays::hashCode))
                .map(RlpString::create).collect(Collectors.toList());

        return Numeric.byteArrayToBase64(
                Hash.sha3(
                        RlpEncoder.encode(new RlpList(rlpList))));
    }
}
