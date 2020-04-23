package org.web3j.protocol.besu.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.filters.Callback;
import org.web3j.protocol.core.filters.Filter;
import org.web3j.protocol.core.filters.FilterException;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;
import org.web3j.protocol.core.methods.response.Log;

public class PrivateLogFilter extends Filter<Log> {

  private final String privacyGroupId;
  private final org.web3j.protocol.core.methods.request.EthFilter ethFilter;
  private String filterId;

  public PrivateLogFilter(
      Besu web3j,
      Callback<Log> callback,
      String privacyGroupId,
      org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
    super(web3j, callback);
    this.privacyGroupId = privacyGroupId;
    this.ethFilter = ethFilter;
  }

  @Override
  protected EthFilter sendRequest() throws IOException {
    EthFilter send = ((Besu) web3j).privNewFilter(privacyGroupId, ethFilter).send();
    this.filterId = send.getResult();
    return send;
  }

  @Override
  protected void process(List<LogResult> logResults) {
    for (EthLog.LogResult logResult : logResults) {
      if (logResult instanceof EthLog.LogObject) {
        Log log = ((EthLog.LogObject) logResult).get();
        callback.onEvent(log);
      } else {
        throw new FilterException(
            "Unexpected result type: " + logResult.get() + " required LogObject");
      }
    }
  }

  @Override
  protected Optional<Request<?, EthLog>> getFilterLogs(BigInteger filterId) {
    return Optional.of(((Besu) web3j).privGetFilterLogs(privacyGroupId, this.filterId));
  }

}
