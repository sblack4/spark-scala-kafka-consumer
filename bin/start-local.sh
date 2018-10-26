#!/bin/bash

SPARK_HOME=/opt/spark-2.1.0-bin-hadoop2.7
KAFKA_HOME=/opt/kafka_2.11-0.10.2.2

function kill_all() {
  pkill -f kafka
  pkill -f spark
}

function start_spark() {
  cd $SPARK_HOME

  ./sbin/start-master.sh 
  ./sbin/start-slave.sh spark://Stevens-MacBook-Pro.local:7077
}

function start_kafka() {
  cd $KAFKA_HOME

  bin/zookeeper-server-start.sh config/zookeeper.properties >> server.out &
  bin/kafka-server-start.sh config/server.properties >> server-kafka.out &
  bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic idcs-c6319aeb031a417c97ac0f048627b112-conga >> topic.out &
}


main() {
  kill_all

  start_spark
  start_kafka
}

main "$@"
