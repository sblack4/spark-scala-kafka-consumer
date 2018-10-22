name := "spark-kafka-consumer"
organization := "com.github.unofficialoraclecloudhub"
version := "1.0"
scalaVersion := "2.11.8"

val sparkVersion = "2.1.0"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion,
    "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1", 
    "org.apache.spark" %% "spark-streaming-kafka-0-8-assembly" % "2.1.1",
    "org.apache.spark" %% "spark-hive" % sparkVersion,
    "log4j" % "log4j" % "1.2.17",
//    "org.json4s" %% "json4s-native" % "3.2.11"
    "org.json4s" %% "json4s-jackson" % "3.2.11"
)
