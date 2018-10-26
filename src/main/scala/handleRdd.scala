package com.github.cloudsolutionhubs.tweetconsumer

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

  /**
    * TODO: Implement
    * see https://developer.twitter.com/ and apps.twitter.com
    *
    * the data is being pumped into kafka by a python app
    * https://github.com/cloudsolutionhubs/twitter-kafka-producer
    * which uses the tweepy library, see
    * http://docs.tweepy.org/en/stable/streaming_how_to.html#step-2-creating-a-stream
    * and gets a list of tweet-objects, see
    * https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/tweet-object
    *
    * @param tweetRDD
    */
  def handleRealTweet(tweetRDD: rdd) = {

  }

}
