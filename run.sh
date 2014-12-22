#!/bin/bash

ARGS="$@"

echo "Running command:"
echo mvn exec:java "'-Dexec.args=${ARGS}'"
mvn exec:java -o "-Dexec.args=${ARGS}"
