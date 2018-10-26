#!/bin/bash


[ "$1" =  DEBUG ] && DEBUG=1

if [ $DEBUG -eq 1 ]; then
  sbt package

  echo "DEBUG: $DEBUG"
  export SPARK_SUBMIT_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005

  pwd=$(pwd)
  broker="129.144.154.83:6667:6667"
  topic="idcs-c6319aeb031a417c97ac0f048627b112-conga"
  stream="tweets"
  
  spark-submit \
      --verbose \
      --master local[3] \
      --class "com.github.unofficialoraclecloudhub.streaming_data_analytics.main" \
      --jars file://${pwd}/lib/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar \
      --jars file://${pwd}/lib/json4s-jackson_2.12-3.2.11.jar \
      file://${pwd}/target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar ${broker} ${topic} ${stream}
  
else 

  pwd=$(pwd)
  broker="129.144.154.83:6667"
  topic="idcs-c6319aeb031a417c97ac0f048627b112-conga"
  stream="tweets"
  
  spark-submit \
      --verbose \
      --master local \
      --class "com.github.unofficialoraclecloudhub.streaming_data_analytics.main" \
      --jars file://${pwd}/target/scala-2.11/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar \
      file://${pwd}/target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar ${broker} ${topic} ${stream}
  
      # --master yarn \
      # --jars file://${pwd}/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar \
      # file://${pwd}/spark-kafka-consumer_2.11-1.0.jar ${broker} ${topic} ${stream}
fi
