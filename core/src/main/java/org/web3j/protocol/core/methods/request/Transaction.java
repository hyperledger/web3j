package org.web3j.protocol.core.methods.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.utils.Numeric;

import org.web3j.protobuf.Blockchain;

import org.web3j.protobuf.ConvertStrByte;
import org.web3j.protobuf.Blockchain.Crypto;

import com.google.protobuf.*;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;

import static org.abstractj.kalium.encoders.Encoder.HEX;

import org.abstractj.kalium.keys.SigningKey;
import org.abstractj.kalium.crypto.Hash;

/**
 * Transaction request object used the below methods.
 * <ol>
 * <li>eth_call</li>
 * <li>eth_sendTransaction</li>
 * <li>eth_estimateGas</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    private String to;
    private BigInteger nonce;  // nonce field is not present on eth_call/eth_estimateGas
    private long quota;  // gas
    private long valid_until_block;
    private int version = 0;
    private String data;
    private int chainId;
    private final Hash hash = new Hash();

    public Transaction(String to, BigInteger nonce, long quota, long valid_until_block, int version, int chainId, String data) {
        this.to = to;
        this.nonce = nonce;
        this.quota = quota;
        this.version = version;
        this.valid_until_block = valid_until_block;
        this.chainId = chainId;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }
    }

    public static Transaction createContractTransaction(
         BigInteger nonce, long quota, long valid_until_block, int version, int chainId, String init) {
        return new Transaction("", nonce, quota, valid_until_block, version, chainId, init);
    }

    public static Transaction createFunctionCallTransaction(
         String to, BigInteger nonce, long quota, long valid_until_block, int version, String data, int chainId) {

        return new Transaction(to, nonce, quota, valid_until_block, version, chainId, data);
    }

    public String getTo() {
        return to;
    }

    public String getNonce() {
        return convert(nonce);
    }

    public long getQuota() {
        return quota;
    }

    public long get_valid_until_block() {
        return valid_until_block;
    }

    public int getVersion() {
        return version;
    }

    public String getData() {
        return data;
    }

    public int getChainId() { return chainId; }


    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.cleanHexPrefix(Numeric.encodeQuantity(value));
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }

    public String sign(String privateKey) {
        return sign(privateKey, false);
    }

    public String sign(String privateKey, boolean isEd25519AndBlake2b) {
        Blockchain.Transaction.Builder builder = Blockchain.Transaction.newBuilder();
        byte[] strbyte = ConvertStrByte.hexStringToBytes(Numeric.cleanHexPrefix(getData()));
        ByteString bdata = ByteString.copyFrom(strbyte);

        builder.setData(bdata);
        builder.setNonce(getNonce());
        builder.setTo(getTo());
        builder.setValidUntilBlock(get_valid_until_block());
        builder.setVersion(getVersion());
        builder.setQuota(getQuota());
        builder.setChainId(getChainId());
        Blockchain.Transaction tx = builder.build();

        byte[] sig;
        if (isEd25519AndBlake2b) {
            byte[] message = hash.blake2(tx.toByteArray(), "CryptapeCryptape".getBytes(), null, null);
            SigningKey key = new SigningKey(privateKey, HEX);
            byte[] pk = key.getVerifyKey().toBytes();
            byte[] signature = key.sign(message);
            sig = new byte[signature.length + pk.length];
            System.arraycopy(signature, 0, sig, 0, signature.length);
            System.arraycopy(pk, 0, sig, signature.length, pk.length);
        } else {
            Credentials credentials = Credentials.create(privateKey);
            ECKeyPair keyPair = credentials.getEcKeyPair();
            Sign.SignatureData signatureData = Sign.signMessage(tx.toByteArray(), keyPair);
            sig = signatureData.get_signature();
        }

        Blockchain.UnverifiedTransaction.Builder builder1 = Blockchain.UnverifiedTransaction.newBuilder();
        builder1.setTransaction(tx);
        builder1.setSignature(ByteString.copyFrom(sig));
        builder1.setCrypto(Crypto.SECP);
        Blockchain.UnverifiedTransaction utx = builder1.build();

        return ConvertStrByte.bytesToHexString(utx.toByteArray());
    }

    // just used to secp256k1
    public String sign(Credentials credentials) {
        Blockchain.Transaction.Builder builder = Blockchain.Transaction.newBuilder();
        byte[] strbyte = ConvertStrByte.hexStringToBytes(Numeric.cleanHexPrefix(getData()));
        ByteString bdata = ByteString.copyFrom(strbyte);

        builder.setData(bdata);
        builder.setNonce(getNonce());
        builder.setTo(getTo());
        builder.setValidUntilBlock(get_valid_until_block());
        builder.setQuota(getQuota());
        builder.setVersion(getVersion());
        builder.setChainId(getChainId());
        Blockchain.Transaction tx = builder.build();

        ECKeyPair keyPair = credentials.getEcKeyPair();
        Sign.SignatureData signatureData = Sign.signMessage(tx.toByteArray(), keyPair);
        byte[] sig = signatureData.get_signature();

        Blockchain.UnverifiedTransaction.Builder builder1 = Blockchain.UnverifiedTransaction.newBuilder();
        builder1.setTransaction(tx);
        builder1.setSignature(ByteString.copyFrom(sig));
        builder1.setCrypto(Crypto.SECP);
        Blockchain.UnverifiedTransaction utx = builder1.build();

        return ConvertStrByte.bytesToHexString(utx.toByteArray());
    }
}
