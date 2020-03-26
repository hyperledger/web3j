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
package org.web3j.protocol.core.methods.response.admin;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

/** admin_peers. */
public class AdminPeers extends Response<List<AdminPeers.Peer>> {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Override
    @JsonDeserialize(using = AdminPeers.ResponseDeserialiser.class)
    public void setResult(final List<Peer> result) {
        super.setResult(result);
    }

    public static class Peer {
        public Peer() {}

        public Peer(String id, String name, String enode, PeerNetwork network) {
            this.id = id;
            this.name = name;
            this.network = network;
            this.enode = enode;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEnode() {
            return enode;
        }

        private String id;
        private String name;
        private PeerNetwork network;
        private String enode;

        public PeerNetwork getNetwork() {
            return network;
        }
    }

    public static class PeerNetwork {

        public PeerNetwork() {}

        private String localAddress;
        private String remoteAddress;

        public PeerNetwork(final String localAddress, final String remoteAddress) {
            this.localAddress = localAddress;
            this.remoteAddress = remoteAddress;
        }

        public String getLocalAddress() {
            return localAddress;
        }

        public String getRemoteAddress() {
            return remoteAddress;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<List<Peer>> {

        private final ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<Peer> deserialize(
                final JsonParser jsonParser, final DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, new TypeReference<List<Peer>>() {});
            } else {
                return null; // null is wrapped by Optional in above getter
            }
        }
    }
}
