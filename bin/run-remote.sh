#!/bin/bash



sbt package

scp target/scala-2.11/spark-kafka-consumer_2.11-1.0.jar bdc:/tmp

ssh bdc "sudo su -c "/home/spark/run.sh " -s /bin/bash spark"

