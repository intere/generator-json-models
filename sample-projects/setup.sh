#!/bin/bash

pwd=$(dirname $0);
cd ${pwd};
CUR_DIR=$(pwd);

nukeTmp() {
  if [ -e ${CUR_DIR}/tmp ] ; then
    rm -rf ${CUR_DIR}/tmp;
  fi
}

generateJava() {
  JAVA_DIR=${CUR_DIR}/java/generated-code;

  nukeTmp;

  ../run.sh --orchestrate ${CUR_DIR}/metadata-java.json \
    -o ${CUR_DIR}/tmp

  rm -rf ${JAVA_DIR}/src/main/java ${JAVA_DIR}/src/test/java
  mv ${CUR_DIR}/tmp/src ${JAVA_DIR}/src/main/java
  mv ${CUR_DIR}/tmp/test ${JAVA_DIR}/src/test/java
}

generateObjectiveC() {
  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-objc.json \
    -o ${CUR_DIR}/tmp
}

main() {
  generateJava;
  generateObjectiveC
}

main;
