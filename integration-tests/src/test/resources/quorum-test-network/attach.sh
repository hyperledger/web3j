#!/bin/bash

MEMBER_NUMBER=$1
MEMBER_NODE="member${MEMBER_NUMBER}quorum"
echo "Attempting to connect to $MEMBER_NODE"
if [ -z `docker compose -f docker-compose.yml ps -q ${MEMBER_NODE} 2>/dev/null` ] ; then
  echo "$MEMBER_NUMBER is not a valid member node. Must be between 1 and 3" >&2
  exit 1
fi

# can also do geth attach http://localhost:8545
docker compose exec ${MEMBER_NODE} /bin/sh -c "geth attach /data/geth.ipc"
