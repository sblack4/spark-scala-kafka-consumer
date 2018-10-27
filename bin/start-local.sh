#!/bin/bash

SPARK_HOME=/opt/spark-2.1.0-bin-hadoop2.7
KAFKA_HOME=/opt/kafka_2.11-0.10.2.2
SPARK_SLAVE="spark://Stevens-MacBook-Pro.local:7077"

function kill_all() {
  echo "killing all previously running instances of spark or kafka"
  pkill -f kafka
  pkill -f spark
}

function start_spark() {
  cd ${SPARK_HOME}

    echo "starting spark master and a local worker"
  ./sbin/start-master.sh 
  ./sbin/start-slave.sh "$SPARK_SLAVE"
}

function start_kafka() {
  cd ${KAFKA_HOME}

  echo "starting zookeeper, kafka, and creating a topic named topical"
  bin/zookeeper-server-start.sh config/zookeeper.properties >> server.out &
  sleep 20
  bin/kafka-server-start.sh config/server.properties >> server-kafka.out &
  sleep 20
  bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topical >> topic.out &
}


main() {
    if [ ${1} == "kill" ]; then
        kill_all
        echo "exiting"
        exit 0
    fi
  kill_all

  start_spark
  start_kafka
  echo "success!"
}

main "$@"
