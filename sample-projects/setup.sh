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

  rm -rf ${JAVA_DIR}/src
  # rm -rf ${JAVA_DIR}/src/main/java ${JAVA_DIR}/src/test/java
  mkdir -p ${JAVA_DIR}/src/main/java/com/intere/test ${JAVA_DIR}/src/test/java/com/intere/test
  cp -r ${CUR_DIR}/tmp/src/* ${JAVA_DIR}/src/main/java
  cp -r ${CUR_DIR}/tmp/test/* ${JAVA_DIR}/src/test/java
}

generateObjectiveC() {
  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-objc.json \
    -o ${CUR_DIR}/tmp

    echo "cp ${CUR_DIR}/tmp/src/* objc/Generated/Generated/src"
    cp ${CUR_DIR}/tmp/src/* objc/Generated/Generated/src
    # echo "cp ${CUR_DIR}/tmp/views/* objc/Generated/Generated/views"
    # cp ${CUR_DIR}/tmp/views/* objc/Generated/Generated/views
    # echo "cp ${CUR_DIR}/tmp/services/* objc/Generated/Generated/services"
    # cp ${CUR_DIR}/tmp/services/* objc/Generated/Generated/services
    echo "cp ${CUR_DIR}/tmp/test/* objc/Generated/GeneratedTests/test"
    cp ${CUR_DIR}/tmp/test/* objc/Generated/GeneratedTests/test
}

generateSwift() {
  nukeTmp;
  ../run.sh --orchestrate ${CUR_DIR}/metadata-swift.json \
	-o ${CUR_DIR}/tmp

  cp ${CUR_DIR}/tmp/src/* swift/Generated/Generated/src/
  cp ${CUR_DIR}/tmp/test/* swift/Generated/GeneratedTests/test/

}

build() {
  cd ../
  mvn clean install -DskipTests
  cd sample-projects
}

main() {
  build;
  generateJava;   # Generated using templates, checks out
  generateRuby;     # Generated using templates, checks out
  generateSwift;  # Generated using templates, checks out
  generateObjectiveC;  # Generated using templates, checks out
}

main;
