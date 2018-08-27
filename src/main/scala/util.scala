package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types
import org.apache.spark.sql.Row
import scala.util.Random
import java.time.format.DateTimeFormatter
import java.time.LocalDate


object util {


    def handleJson(df: Dataset[classes.Tweet]) = {
        val tweets = df
            .select(
                "tweet_date",
                "text",
                "city"
            )

        tweets.write.mode("append").insertInto("default.tweets")
    }
}