package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * @author Edison
 * @version 1.0
 * <p></p>
 * @date 2023/2/8
 * @since 17
 */
public class EthGetBlockReceipts extends Response<List<TransactionReceipt>> {
    public Optional<List<TransactionReceipt>> getBlockReceipts() {
        return Optional.ofNullable(getResult());
    }

}
