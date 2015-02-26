#!/bin/bash

CURDIR=$(dirname $0);
cd ${CURDIR};

#
# YES: We eat our own dog food!
#
generateMetadataJson() {
  # ./run.sh -cn Metadata -f src/test/resources/metadata/metadata.json \
  #   -o tmp  -l java -ns com.intere.generator.metadata
  # ./run.sh -cn ModelClass -f src/test/resources/metadata/class.json \
  #   -o tmp  -l java -ns com.intere.generator.metadata

  ./run.sh -o tmp --orchestrate src/test/resources/metadata/class_metadata.json

  mkdir -p src/com/intere/generator/metadata
  cp tmp/src/com/intere/generator/metadata/* src/main/java/com/intere/generator/metadata

  mkdir -p test/com/intere/generator/metadata
  cp tmp/test/com/intere/generator/metadata/* src/test/java/com/intere/generator/metadata

  # mkdir -p src/test/resources/com/intere/generator/metadata
  # cp tmp/src/test/resources/com/intere/generator/metadata/* src/test/resources/com/intere/generator/metadata
}

generateMetadataJson;
