/*
 * Copyright 2022 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core.methods.response;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/** eth_feeHistory. */
public class EthFeeHistory extends Response<EthFeeHistory.FeeHistory> {

    @Override
    @JsonDeserialize(using = EthFeeHistory.ResponseDeserialiser.class)
    public void setResult(FeeHistory result) {
        super.setResult(result);
    }

    public FeeHistory getFeeHistory() {
        return getResult();
    }

    public static class FeeHistory {
        private String oldestBlock;
        private List<List<String>> reward;
        private List<String> baseFeePerGas;
        private List<Double> gasUsedRatio;

        public FeeHistory() {}

        public FeeHistory(
                String oldestBlock,
                List<List<String>> reward,
                List<String> baseFeePerGas,
                List<Double> gasUsedRatio) {
            this.oldestBlock = oldestBlock;
            this.reward = reward;
            this.baseFeePerGas = baseFeePerGas;
            this.gasUsedRatio = gasUsedRatio;
        }

        public BigInteger getOldestBlock() {
            return Numeric.decodeQuantity(oldestBlock);
        }

        public String getOldestBlockRaw() {
            return oldestBlock;
        }

        public void setOldestBlock(String oldestBlock) {
            this.oldestBlock = oldestBlock;
        }

        public List<List<BigInteger>> getReward() {
            List<List<BigInteger>> retValue = new ArrayList<>(reward.size());
            for (int i = 0; i < reward.size(); i++) {
                List<String> r = reward.get(i);
                List<BigInteger> tmp = new ArrayList<>(r.size());
                for (int j = 0; j < r.size(); j++) {
                    tmp.add(Numeric.decodeQuantity(r.get(j)));
                }
                retValue.add(tmp);
            }
            return retValue;
        }

        public void setReward(List<List<String>> reward) {
            this.reward = reward;
        }

        public List<List<String>> getRewardRaw() {
            return reward;
        }

        public List<BigInteger> getBaseFeePerGas() {
            List<BigInteger> retValue = new ArrayList<>(baseFeePerGas.size());
            for (int i = 0; i < baseFeePerGas.size(); i++) {
                retValue.add(Numeric.decodeQuantity(baseFeePerGas.get(i)));
            }
            return retValue;
        }

        public void setBaseFeePerGas(List<String> baseFeePerGas) {
            this.baseFeePerGas = baseFeePerGas;
        }

        public List<String> getBaseFeePerGasRaw() {
            return baseFeePerGas;
        }

        public List<Double> getGasUsedRatio() {
            return gasUsedRatio;
        }

        public void setGasUsedRatio(List<Double> gasUsedRatio) {
            this.gasUsedRatio = gasUsedRatio;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FeeHistory)) {
                return false;
            }

            FeeHistory feeHistory = (FeeHistory) o;

            if (getOldestBlockRaw() != null
                    ? !getOldestBlockRaw().equals(feeHistory.getOldestBlockRaw())
                    : feeHistory.getOldestBlockRaw() != null) {
                return false;
            }

            if (getRewardRaw() != null
                    ? !getRewardRaw().equals(feeHistory.getRewardRaw())
                    : feeHistory.getRewardRaw() != null) {
                return false;
            }

            if (getBaseFeePerGasRaw() != null
                    ? !getBaseFeePerGasRaw().equals(feeHistory.getBaseFeePerGasRaw())
                    : feeHistory.getBaseFeePerGasRaw() != null) {
                return false;
            }

            return getGasUsedRatio() != null
                    ? getGasUsedRatio().equals(feeHistory.getGasUsedRatio())
                    : feeHistory.getGasUsedRatio() == null;
        }

        @Override
        public int hashCode() {
            int result = getOldestBlockRaw() != null ? getOldestBlockRaw().hashCode() : 0;
            result = 31 * result + (getRewardRaw() != null ? getRewardRaw().hashCode() : 0);
            result =
                    31 * result
                            + (getBaseFeePerGasRaw() != null
                                    ? getBaseFeePerGasRaw().hashCode()
                                    : 0);
            result = 31 * result + (getGasUsedRatio() != null ? getGasUsedRatio().hashCode() : 0);
            return result;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<FeeHistory> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public FeeHistory deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, FeeHistory.class);
            } else {
                return null; // null is wrapped by Optional in above getter
            }
        }
    }
}
