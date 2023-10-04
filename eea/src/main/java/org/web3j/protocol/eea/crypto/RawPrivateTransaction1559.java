package org.web3j.protocol.eea.crypto;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.transaction.type.ITransaction;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Base64String;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;
import org.web3j.utils.Restriction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RawPrivateTransaction1559 extends RawPrivateTransaction{

    private long chainId;
    private BigInteger maxPriorityFeePerGas;
    private BigInteger maxFeePerGas;


    protected RawPrivateTransaction1559(long chainId,
                                        BigInteger nonce,
                                        BigInteger gasLimit,
                                        String to,
                                        String data,
                                        BigInteger maxPriorityFeePerGas,
                                        BigInteger maxFeePerGas,
                                        Base64String privateFrom,
                                        List<Base64String> privateFor,
                                        Base64String privacyGroupId,
                                        Restriction restriction) {
        super(nonce, null, gasLimit, to, data, privateFrom, privateFor, privacyGroupId, restriction);
        this.chainId = chainId;
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
        this.maxFeePerGas = maxFeePerGas;
    }

    protected RawPrivateTransaction1559(
            long chainId,
            BigInteger nonce,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {
        this(chainId, nonce, gasLimit, to, data, maxPriorityFeePerGas, maxFeePerGas, privateFrom, null, privacyGroupId, restriction);
    }

    protected RawPrivateTransaction1559(
            long chainId,
            BigInteger nonce,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Restriction restriction) {
        this(chainId, nonce, gasLimit, to, data, maxPriorityFeePerGas, maxFeePerGas, privateFrom, privateFor, null, restriction);
    }

    @Override
    public List<RlpType> asRlpValues(
            final Sign.SignatureData signatureData) {

        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(getChainId()));

        result.add(RlpString.create(getNonce()));

        // add maxPriorityFeePerGas and maxFeePerGas if this is an EIP-1559 transaction
        result.add(RlpString.create(getMaxPriorityFeePerGas()));
        result.add(RlpString.create(getMaxFeePerGas()));

        result.add(RlpString.create(getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = getTo();
        if (to != null && to.length() > 0) {
            // addresses that start with zeros should be encoded with the zeros included, not
            // as numeric values
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(getValue()));

        // value field will already be hex encoded, so we need to convert into binary first
        byte[] data = Numeric.hexStringToByteArray(getData());
        result.add(RlpString.create(data));

        // access list
        result.add(new RlpList());

        if (signatureData != null) {
            result.add(RlpString.create(Sign.getRecId(signatureData, getChainId())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        result.add(getPrivateFrom().asRlp());

        getPrivateFor()
                .ifPresent(privateFor -> result.add(Base64String.unwrapListToRlp(privateFor)));

        getPrivacyGroupId().map(Base64String::asRlp).ifPresent(result::add);

        result.add(RlpString.create(getRestriction().getRestriction()));

        return result;
    }


    @Override
    public BigInteger getGasPrice() {
        throw new UnsupportedOperationException("not available for 1559 transaction");
    }

    public long getChainId() {
        return chainId;
    }

    public BigInteger getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public BigInteger getMaxFeePerGas() {
        return maxFeePerGas;
    }
}
