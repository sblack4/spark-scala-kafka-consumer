package com.github.cloudsolutionhubs.tweetconsumer

import org.apache.spark.sql.SparkSession
import _root_.kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.rdd.RDD


object main {
  def main(args: Array[String]): Unit = {
    @transient lazy val log: Logger = LogManager.getLogger("mainLogger")

    val configFilePath = args(0)
    val numberOfSeconds = 5

    val spark = SparkSession.builder()
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._

    val fileText = util.fileText(spark, configFilePath)
    val appConfig: classes.appConfig = util.parseConfig(fileText)
    val isStreamSurvey = util.isSurveyStream(appConfig.stream)

    util.createTables(spark)
    val ssc = new StreamingContext(spark.sparkContext, Seconds(numberOfSeconds))

    //https://spark.apache.org/docs/1.6.1/streaming-kafka-integration.html
    val topicsSet = appConfig.topic.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> appConfig.broker)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    //for debugging, you can print the full contents of the first 10 rows of each batch of messages by uncommenting the following
    messages.print()

    /**
      * TODO: add logic to handle real tweets.
      * this is a design decision - how should you pass this information to the program?
      * or should the program infer it at runtime?
      * instead of passing it in as an argument maybe try pattern matching
      */
    messages.foreachRDD((rdd: RDD[(String, String)]) => {
      val df = spark.read.json(rdd.map(x => x._2))

      if (isStreamSurvey) {
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