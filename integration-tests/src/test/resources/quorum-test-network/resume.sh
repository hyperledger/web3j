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

NO_LOCK_REQUIRED=false

. ./.env
. ./.common.sh

echo "${bold}*************************************"
echo "Quorum Dev Quickstart "
echo "*************************************${normal}"
echo "Resuming network..."
echo "----------------------------------"

if [ -f "docker-compose-deps.yml" ]; then
    echo "Starting dependencies..."
    docker compose -f docker-compose-deps.yml start
    sleep 60
fi

docker compose start

