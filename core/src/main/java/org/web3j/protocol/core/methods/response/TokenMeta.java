/*
 * Copyright 2019 Web3 Labs Ltd.
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

public class TokenMeta {
    private String name;
    private String symbol;
    private Integer decimals;
    private String tokenType;

    public TokenMeta() {
    }

    public TokenMeta(String name, String symbol, int decimals, String tokenType) {
      this.name = name;
      this.symbol = symbol;
      this.decimals = decimals;
      this.tokenType = tokenType;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getSymbol() {
      return symbol;
    }

    public void setSymbol(String symbol) {
      this.symbol = symbol;
    }

    public Integer getDecimals() {
      return decimals;
    }

    public void setDecimals(Integer decimals) {
      this.decimals = decimals;
    }

    public String getTokenType() {
      return tokenType;
    }

    public void setTokenType(String tokenType) {
      this.tokenType = tokenType;
    }

    @Override
    public int hashCode() {
      int result = getTokenType() != null ? getTokenType().hashCode() : 0;
      result = 31 * result + (getDecimals() != null ? getDecimals().hashCode() : 0);
      result = 31 * result + (getSymbol() != null ? getDecimals().hashCode() : 0);
      result = 31 * result + (getName() != null ? getName().hashCode() : 0);
      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof TokenMeta)) {
        return false;
      }

      TokenMeta that = (TokenMeta) o;

      if (getTokenType() != that.getTokenType()) {
        return false;
      }
      if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
        return false;
      }
      if (getDecimals() != null
          ? !getDecimals().equals(that.getDecimals())
          : that.getDecimals() != null) {
        return false;
      }
      return getSymbol() != null ? getSymbol().equals(that.getSymbol()) : that.getSymbol() == null;
    }

    @Override
    public String toString() {
      return "TokenMeta{" +
          "name='" + name + '\'' +
          ", symbol='" + symbol + '\'' +
          ", decimals=" + decimals +
          ", tokenType='" + tokenType + '\'' +
          '}';
    }
}