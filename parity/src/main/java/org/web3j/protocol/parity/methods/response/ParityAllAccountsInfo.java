/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.protocol.parity.methods.response;

import java.util.Map;

import org.web3j.protocol.core.Response;

/** parity_allAccountsInfo. */
public class ParityAllAccountsInfo
        extends Response<Map<String, ParityAllAccountsInfo.AccountsInfo>> {

    // we need to use a map type as a string value is returned with the account information
    public Map<String, AccountsInfo> getAccountsInfo() {
        return getResult();
    }

    public static class AccountsInfo {
        private String name;
        private String uuid;
        private Map<String, Object> meta;

        public AccountsInfo() {}

        public AccountsInfo(Map<String, Object> meta, String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
            this.meta = meta;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Map<String, Object> getMeta() {
            return meta;
        }

        public void setMeta(Map<String, Object> meta) {
            this.meta = meta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AccountsInfo)) {
                return false;
            }

            AccountsInfo that = (AccountsInfo) o;

            if (name != null ? !name.equals(that.name) : that.name != null) {
                return false;
            }
            if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) {
                return false;
            }
            return meta != null ? meta.equals(that.meta) : that.meta == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
            result = 31 * result + (meta != null ? meta.hashCode() : 0);
            return result;
        }
    }
}
