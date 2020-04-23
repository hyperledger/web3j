package org.web3j.protocol.besu.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.filters.Callback;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.utils.Numeric;

public class PrivateLogFilter extends LogFilter {

  private final String privacyGroupId;

  public PrivateLogFilter(
      Besu web3j,
      Callback<Log> callback,
      String privacyGroupId,
      org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
    super(web3j, callback, ethFilter);
    this.privacyGroupId = privacyGroupId;
  }

  @Override
  protected EthFilter sendRequest() throws IOException {
    return ((Besu) web3j).privNewFilter(privacyGroupId, ethFilter).send();
  }

  @Override
  protected EthUninstallFilter uninstallFilter(BigInteger filterId) throws IOException {
    return ((Besu) web3j)
        .privUninstallFilter(privacyGroupId, Numeric.toHexStringWithPrefix(filterId)).send();
  }

  @Override
  protected Optional<Request<?, EthLog>> getFilterLogs(BigInteger filterId) {
    return Optional.of(((Besu) web3j)
        .privGetFilterLogs(privacyGroupId, Numeric.toHexStringWithPrefix(filterId)));
  }

}
