#!/bin/bash

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP: 
./nameserver.sh ip_address
	- remote_server_ip: (OPTIONAL) l'addresse ip du serveur distant

EndOfMessage

java -cp "$basepath"/nameserver.jar:"$basepath"/shared.jar -Djava.security.policy="$basepath"/policy ca.polymtl.inf8480.tp1.client.NameServer $*
