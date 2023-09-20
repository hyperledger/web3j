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
NO_LOCK_REQUIRED=false

cd src/test/resources/quorum-test-network/ || exit

. ./.env
. ./.common.sh

removeDockerImage(){
  if [[ ! -z `docker ps -a | grep $1` ]]; then
    docker image rm $1
  fi
}

echo "*************************************"
echo "Quorum Dev Quickstart "
echo "*************************************"
echo "Stop and remove network..."

docker compose down -v
docker compose rm -sfv

if [ -f "docker-compose-deps.yml" ]; then
    echo "Stopping dependencies..."
    docker compose -f docker-compose-deps.yml down -v
    docker compose rm -sfv
fi
# pet shop dapp
if [[ ! -z `docker ps -a | grep quorum-dev-quickstart_pet_shop` ]]; then
  docker stop quorum-dev-quickstart_pet_shop
  docker rm quorum-dev-quickstart_pet_shop
  removeDockerImage quorum-dev-quickstart_pet_shop
fi

if grep -q 'kibana:' docker-compose.yml 2> /dev/null ; then
  docker image rm quorum-test-network_elasticsearch
  docker image rm quorum-test-network_logstash
  docker image rm quorum-test-network_filebeat
  docker image rm quorum-test-network_metricbeat
fi

rm ${LOCK_FILE}
echo "Lock file ${LOCK_FILE} removed"
