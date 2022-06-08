#!/usr/bin/env bash
set -e

FLAG="$1"
SERVER_LOGFILE_NAME="server.log"
CLIENT_LOGFILE="client.log"

trap "jobs -p | xargs kill || true" EXIT

ds-server -v all -c ./test/S2DemoConfigs/config50-long-high.xml &>$SERVER_LOGFILE_NAME &

sleep 0.05

if [[ $FLAG == "java" ]]; then
    java -jar ./target/ds-sim-1.0-SNAPSHOT.jar &> $CLIENT_LOGFILE
elif [[ $FLAG == "c" ]]; then
	./ds-sim/src/pre-compiled/ds-client -v -a "${2:-lrr}"
fi

if [[ $FLAG == '-v' ]]; then
	cat $SERVER_LOGFILE_NAME

fi

tail -n 4 $SERVER_LOGFILE_NAME 
