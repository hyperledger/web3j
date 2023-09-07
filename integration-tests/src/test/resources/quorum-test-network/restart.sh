#!/bin/bash

./stop.sh
echo "Waiting 30s for containers to stop"
sleep 30
./resume.sh
