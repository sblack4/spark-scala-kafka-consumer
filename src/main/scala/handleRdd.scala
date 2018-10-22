package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.rdd
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.{col, expr}


object handleRdd {

  def handleTweet(df: Dataset[classes.Tweet]): Unit = {
    val tweets = df
      .select(
        expr("day as tweet_date"),
        col("tweet"),
        col("city")
      )

    tweets.write.mode("append")
      .insertInto("default.tweets")
  }

  def handleSurvey(df: Dataset[classes.Survey]): Unit = {
    val tweets = df
      .select(
        expr("date as survey_date"),
        col("rating"),
        col("city")
      )

    tweets.write.mode("append")
      .insertInto("default.survey")
  }

  def handleRealTweet(tweetRDD: rdd) = {

  }

}
