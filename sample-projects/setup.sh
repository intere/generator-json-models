#!/bin/bash

pwd=$(dirname $0);
cd ${pwd};
CUR_DIR=$(pwd);

nukeTmp() {
  if [ -e ${CUR_DIR}/tmp ] ; then
    rm -rf ${CUR_DIR}/tmp;
  fi
}

generateRuby() {
  RUBY_DIR=${CUR_DIR}/ruby;
  RUBY_MODELS=${RUBY_DIR}/app/models;
  RUBY_SPECS=${RUBY_DIR}/spec/models;

  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-ruby.json \
    -o ${CUR_DIR}/tmp
  cp ${CUR_DIR}/tmp/src/* ${RUBY_MODELS};
  if [ ! -e ${RUBY_SPECS} ] ; then
    mkdir -p ${RUBY_SPECS};
  fi
  cp ${CUR_DIR}/tmp/test/* ${RUBY_SPECS};
}

generateJava() {
  JAVA_DIR=${CUR_DIR}/java/generated-code;

  nukeTmp;

  ../run.sh --orchestrate ${CUR_DIR}/metadata-java.json \
    -o ${CUR_DIR}/tmp

  rm -rf ${JAVA_DIR}/src/main/java ${JAVA_DIR}/src/test/java
  cp -r ${CUR_DIR}/tmp/src ${JAVA_DIR}/src/main/java
  cp -r ${CUR_DIR}/tmp/test ${JAVA_DIR}/src/test/java
}

generateObjectiveC() {
  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-objc.json \
    -o ${CUR_DIR}/tmp
  cp ${CUR_DIR}/tmp/src/* objc/Generated/Generated/
  cp ${CUR_DIR}/tmp/views/* objc/Generated/Generated/
  cp ${CUR_DIR}/tmp/services/* objc/Generated/Generated/
  cp ${CUR_DIR}/tmp/test/* objc/Generated/GeneratedTests/
}

generateSwift() {
  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-swift.json \
	-o ${CUR_DIR}/tmp
  
}

main() {
  # generateJava;
  generateObjectiveC;
  # generateRuby;
  # generateSwift;
}

main;
