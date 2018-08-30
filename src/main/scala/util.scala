package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.{expr, col}
import org.apache.spark.sql.SparkSession


object util {

    def createTables(spark: SparkSession) = {
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
    }

    def handleTweet(df: Dataset[classes.Tweet]) = {
        val tweets = df
            .select(
                expr("date as tweet_date"),
                col("text"),
                col("city")
            )

        tweets.write.mode("append")
            .insertInto("default.tweets")
    }

    def handleSurvey(df: Dataset[classes.Survey]) = {
        val tweets = df
            .select(
                expr("date as survey_date"),
                col("rating"),
                col("city")
            )

        tweets.write.mode("append")
            .insertInto("default.survey")
    }
}