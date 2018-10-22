package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.SparkSession
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{LogManager, Logger}


object main {
    def main(args: Array[String]){
        @transient lazy val log: Logger = LogManager.getLogger("mainLogger")

      val configFilePath = args(0)

      val spark = SparkSession.builder()
          .enableHiveSupport()
          .getOrCreate()
      import spark.implicits._

      val fileText = util.fileText(spark, configFilePath)
      val appConfig: classes.appConfig = util.parseConfig(fileText)
      val isStreamSurvey = util.isSurveyStream(appConfig.stream)

      util.createTables(spark)
      val ssc = new StreamingContext(spark.sparkContext, Seconds(5))

      //https://spark.apache.org/docs/1.6.1/streaming-kafka-integration.html
      val topicsSet = appConfig.topic.split(",").toSet
      val kafkaParams = Map[String, String]("metadata.broker.list" -> appConfig.broker)
      val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

      //for debugging, you can print the full contents of the first 10 rows of each batch of messages by uncommenting the following
      messages.print()

      messages.foreachRDD(rdd => {
          val df=spark.read.json(rdd.map(x => x._2))
          if(isStreamSurvey){
              val ds = df.as[classes.Survey]
              handleRdd.handleSurvey(ds)
          } else {
              val ds = df.as[classes.Tweet]
              handleRdd.handleTweet(ds)
          }
      })

      println("Starting Streaming Context")
      ssc.start()
      ssc.awaitTermination()
    }
}