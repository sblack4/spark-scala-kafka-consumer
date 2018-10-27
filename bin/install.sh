#!/bin/bash


DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PARENT_DIR="$(dirname "${DIR}")"

cd ${PARENT_DIR}
mkdir lib
cd lib
wget http://central.maven.org/maven2/org/json4s/json4s-jackson_2.12/3.2.11/json4s-jackson_2.12-3.2.11.jar
wget http://central.maven.org/maven2/org/apache/spark/spark-streaming-kafka-0-8-assembly_2.11/2.1.1/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar

cd ${PARENT_DIR}

