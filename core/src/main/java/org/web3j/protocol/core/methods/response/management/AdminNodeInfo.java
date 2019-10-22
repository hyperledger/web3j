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
package org.web3j.protocol.core.methods.response.management;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.core.Response;

/** net_peerCount. */
public class AdminNodeInfo extends Response<AdminNodeInfo.Result> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Override
    @JsonDeserialize(using = AdminNodeInfo.ResponseDeserialiser.class)
    public void setResult(AdminNodeInfo.Result result) {
        super.setResult(result);
    }

    public static class Result {
        public String getName() {
            return name;
        }

        private String name;

        Result(String name) {
            this.name = name;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<AdminNodeInfo.Result> {

        @Override
        public AdminNodeInfo.Result deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            TreeNode treeNode = jsonParser.readValueAsTree();
            return new Result(treeNode.at("/name").toString().replaceAll("^\"|\"$", ""));
        }
    }
}
