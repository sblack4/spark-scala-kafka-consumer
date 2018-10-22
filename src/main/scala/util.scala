package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.log4j.{LogManager, Logger}
import org.json4s._
import org.json4s.jackson.JsonMethods._
//import org.json4s.native.JsonMethods._


object util {
  @transient lazy val log: Logger = LogManager.getLogger("myLogger")

  def fileText(spark: SparkSession, filePath: String): String = {
    val txtdf = spark.read.textFile(filePath)
    txtdf.rdd.reduce(_+_)
  }

  def parseConfig(fileText: String): classes.appConfig = {
    implicit val formats = DefaultFormats
    parse(fileText).extract[classes.appConfig]
  }

    def createTables(spark: SparkSession): DataFrame = {
        val createTweets = """
        CREATE TABLE IF NOT EXISTS tweets (
            tweet_date String,
            text String,
            city String
        )
        """
        spark.sql(createTweets)

        val createSurvey = """
        CREATE TABLE IF NOT EXISTS survey (
            survey_date String,
            rating String,
            city String
        )
        """
        spark.sql(createSurvey)

      val realTweet = """
        CREATE TABLE IF NOT EXISTS realTweets (

        )
        """
      spark.sql(createSurvey)
    }


    def isSurveyStream(streamName: String): Boolean = {
      var streamSurvey = true
      try {
        streamSurvey = streamName.contains("survey")
      } catch {
        case e: Exception =>
          println("something went wrong getting the stream, defaulting to tweets " + e.printStackTrace())
      }
      log.info("streaming survey: " + streamSurvey)

      streamSurvey
    }


}