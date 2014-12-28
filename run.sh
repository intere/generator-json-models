#!/bin/bash

CUR_DIR=$(dirname $0)
cd ${CUR_DIR};
ARGS="$@"

#echo "Running command:"
#echo mvn exec:java -o "'-Dexec.args=${ARGS}'"
mvn exec:java -o "-Dexec.args=${ARGS}"
