#!/bin/bash -u

# Copyright 2018 ConsenSys AG.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
# the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
# an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
# specific language governing permissions and limitations under the License.
TERM=dumb
NO_LOCK_REQUIRED=true

cd src/test/resources/quorum-test-network/ || exit

. ./.env
. ./.common.sh

# create log folders with the user permissions so it won't conflict with container permissions
mkdir -p logs/besu logs/quorum logs/tessera

# Build and run containers and network
echo "docker-compose.yml" > ${LOCK_FILE}

echo "*************************************"
echo "Quorum Dev Quickstart"
echo "*************************************"
echo "Start network"
echo "--------------------"

if [ -f "docker-compose-deps.yml" ]; then
    echo "Starting dependencies..."
    docker compose -f docker-compose-deps.yml up --detach
    sleep 60
fi

echo "Starting network..."
docker compose build --pull
docker compose up --detach

# Sleep for 2 minutes (120 seconds)
sleep 120

# Run the docker ps -a command and capture its output
docker_ps_output=$(docker ps -a)
echo "$docker_ps_output"

# Define the JSON-RPC request as a variable
json_rpc_request='{
  "jsonrpc": "2.0",
  "method": "web3_clientVersion",
  "params": [],
  "id": 1
}'

# Send the JSON-RPC request using curl and capture the response in a variable
response=$(curl -X POST -H "Content-Type: application/json" --data "$json_rpc_request" http://127.0.0.1:8545)

# Print the response
echo "JSON-RPC Response:"
echo "$response"
