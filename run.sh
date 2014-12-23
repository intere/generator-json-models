#!/bin/bash

ARGS="$@"

echo "Running command:"
echo mvn exec:java -o "'-Dexec.args=${ARGS}'"
mvn exec:java -o "-Dexec.args=${ARGS}"
