#!/bin/bash


pwd=$(pwd)
broker="129.144.154.83:6667"
topic="idcs-c6319aeb031a417c97ac0f048627b112-conga"
stream="tweets"

spark-submit \
    --verbose \
    --master yarn \
    --class "com.github.unofficialoraclecloudhub.streaming_data_analytics.main" \
    --jars file://${pwd}/spark-streaming-kafka-0-8-assembly_2.11-2.1.1.jar \
    file://${pwd}/spark-kafka-consumer_2.11-1.0.jar ${broker} ${topic} ${stream}
