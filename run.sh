#!/usr/bin/env bash
set -e

FLAG="$1"
SERVER_LOGFILE_NAME="server.log"
CLIENT_LOGFILE="client.log"

trap "jobs -p | xargs kill || true" EXIT
rm $SERVER_LOGFILE_NAME
./ds-sim/src/ds-server -v all -c ./test/S2DemoConfigs/config50-long-high.xml >> $SERVER_LOGFILE_NAME 2>&1 &

sleep 0.05

if [[ $FLAG == "java" ]]; then
    java -Djava.util.logging.config.file=src/main/resources/logging.properties -jar ./target/ds-client-0.1.jar -a LERT  2>&1 | tee $CLIENT_LOGFILE
elif [[ $FLAG == "c" ]]; then
	./ds-sim/src/pre-compiled/ds-client -v -a "${2:-lrr}"
fi

if [[ $FLAG == '-v' ]]; then
	cat $SERVER_LOGFILE_NAME

fi

tail -n 4 $SERVER_LOGFILE_NAME 
