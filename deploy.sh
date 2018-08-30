#!/bin/bash

sbt package

scp run.sh target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar bdc:/tmp
