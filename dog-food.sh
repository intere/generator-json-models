#!/bin/bash

CURDIR=$(dirname $0);
cd ${CURDIR};

#
# YES: We eat our own dog food!
#
generateMetadataJson() {
  ./run.sh -cn Metadata -f src/test/resources/metadata/metadata.json \
    -o tmp  -l java -ns com.intere.generator.metadata
  ./run.sh -cn ModelClass -f src/test/resources/metadata/class.json \
    -o tmp  -l java -ns com.intere.generator.metadata

  # ./run.sh -o tmp --orchestrate src/test/resources/metadata/class_metadata.json

  mkdir -p src/main/java/com/intere/generator/metadata
  cp tmp/src/main/java/com/intere/generator/metadata/* src/main/java/com/intere/generator/metadata

  mkdir -p src/test/java/com/intere/generator/metadata
  cp tmp/src/test/java/com/intere/generator/metadata/* src/test/java/com/intere/generator/metadata

  mkdir -p src/test/resources/com/intere/generator/metadata
  cp tmp/src/test/resources/com/intere/generator/metadata/* src/test/resources/com/intere/generator/metadata
}

generateMetadataJson;
