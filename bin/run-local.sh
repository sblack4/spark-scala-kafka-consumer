#!/bin/bash


BROKER="http://127.0.0.1:6667"
TOPIC="topical"
STREAM="tweets"
CLASS="com.github.cloudsolutionhubs.tweetconsumer.main"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
PARENT_DIR="$(dirname "${DIR}")"
DEBUG_PORT=5005

function debug(){
  echo "DEBUG ENABLED, Job will listen on port ${DEBUG_PORT}"
  export SPARK_SUBMIT_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=${DEBUG_PORT}
}

main() {
  [ "$1" =  DEBUG ] && debug
  sbt package


  spark-submit \
      --verbose \
      --master local[3] \
      --class "${CLASS}" \
      --jars file://${PARENT_DIR}/lib/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar \
      --jars file://${PARENT_DIR}/lib/json4s-jackson_2.12-3.2.11.jar \
      file://${PARENT_DIR}/target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar ${BROKER} ${TOPIC} ${STREAM}
}

main "$@"
