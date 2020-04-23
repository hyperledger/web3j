package org.web3j.protocol.besu.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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

public class PrivateLogsFilter extends Filter<List<Log>> {

  private final String privacyGroupId;
  private final org.web3j.protocol.core.methods.request.EthFilter ethFilter;

  public PrivateLogsFilter(
      Besu web3j,
      Callback<List<Log>> callback,
      String privacyGroupId,
      org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
    super(web3j, callback);
    this.privacyGroupId = privacyGroupId;
    this.ethFilter = ethFilter;
  }

  @Override
  protected EthFilter sendRequest() throws IOException {
    return ((Besu) web3j).privNewFilter(privacyGroupId, ethFilter).send();
  }

  @Override
  protected void process(List<LogResult> logResults) {
    List<Log> logs = new ArrayList<>(logResults.size());

    for (EthLog.LogResult logResult : logResults) {
      if (!(logResult instanceof EthLog.LogObject)) {
        throw new FilterException(
            "Unexpected result type: " + logResult.get() + " required LogObject");
      }

      logs.add(((EthLog.LogObject) logResult).get());
    }

    callback.onEvent(logs);
  }

  @Override
  protected Optional<Request<?, EthLog>> getFilterLogs(BigInteger filterId) {
    return Optional.of(((Besu) web3j).privGetFilterLogs(privacyGroupId, ""));
  }

}
