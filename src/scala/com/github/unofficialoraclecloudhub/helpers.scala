package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types
import org.apache.spark.sql.Row
import scala.util.Random
import java.time.format.DateTimeFormatter
import java.time.LocalDate


object App {

    /**
     * Parses dates (created_at from twitter) of format: "Thu Feb 15 22:00:54 +0000 2018",
     * this method doesn't allow for use as UDF
     */
    def parseDate(dateStr: String): LocalDate = {
        val dateFormat = DateTimeFormatter.ofPattern("c L d H:m:s Z y")
        try {
            return LocalDate.parse(dateStr, dateFormat)
        } catch {
            case e:Exception=> return LocalDate.now()
        }
    }
    val states = Array("Utah", "Hawaii", "Minnesota", "Ohio", "Arkansas", "Oregon", "Texas", 
        "North Dakota", "Pennsylvania", "Connecticut", "Nebraska", "Vermont", "Nevada", 
        "Washington", "Illinois", "Oklahoma", "District of Columbia", "Delaware", 
        "Alaska", "New Mexico", "West Virginia", "Missouri", "Rhode Island", "Georgia", 
        "Montana", "Michigan", "Virginia", "North Carolina", "Wyoming", "Kansas", 
        "New Jersey", "Maryland", "Alabama", "Arizona", "Iowa", "Massachusetts", 
        "Kentucky", "Louisiana", "Mississippi", "New Hampshire", "Tennessee", 
        "Florida", "Indiana", "Idaho", "South Carolina", "South Dakota", "California", 
        "New York", "Wisconsin", "Colorado", "Maine"
    )
    val randState = () => states.lift(Random.nextInt(states.size)).toString
    val randStateUdf = udf(randState)

    val randProvider = () => List("A", "B", "C")(Random.nextInt(3))
    val randProviderUdf = udf(randProvider)
    // def randState(): String = {
    //     val states = List("Utah", "Hawaii", "Minnesota", "Ohio", "Arkansas", "Oregon", "Texas", 
    //         "North Dakota", "Pennsylvania", "Connecticut", "Nebraska", "Vermont", "Nevada", 
    //         "Washington", "Illinois", "Oklahoma", "District of Columbia", "Delaware", 
    //         "Alaska", "New Mexico", "West Virginia", "Missouri", "Rhode Island", "Georgia", 
    //         "Montana", "Michigan", "Virginia", "North Carolina", "Wyoming", "Kansas", 
    //         "New Jersey", "Maryland", "Alabama", "Arizona", "Iowa", "Massachusetts", 
    //         "Kentucky", "Louisiana", "Mississippi", "New Hampshire", "Tennessee", 
    //         "Florida", "Indiana", "Idaho", "South Carolina", "South Dakota", "California", 
    //         "New York", "Wisconsin", "Colorado", "Maine"
    //     )
    //     return states(Random.nextInt(states.size))
    // }

    // def randProvider():String = {
    //     return List("A", "B", "C")(Random.nextInt(3))
    // }

    def handleJson(df: Dataset[Tweet]) = {
        val filtered_df = df
            .filter("possibly_sensitive = false")
            .map(row => Row(
                row.id,
                row.text,
                row.favorite_count,
                row.retweet_count,
                row.quote_count,
                row.reply_count,
                row.lang,
                row.user.id,
                parseDate(row.created_at).alias("datetime"),
                randState().alias("state"),
                randProvider().alias("provider")
            )
        )

        val filtered_df = df.select(
            col("id"),
            expr("COALESCE(text, \"null\") AS text"),
            expr("COALESCE(favorite_count, 0) AS favorite_count"),
            expr("COALESCE(retweet_count, 0) AS retweet_count"),
            expr("COALESCE(quote_count, 0) AS quote_count"),
            expr("COALESCE(reply_count, 0) as reply_count"),
            expr("COALESCE(lang, \"und\") as lang"),
            expr("COALESCE(coordinates, 0) as coordinates"),
            expr("COALESCE(place, \"null\") as place"),
            col("user.id").alias("user_id"),
            expr("datetime"),
            expr("state"),
            expr("provider")
        )
        tweets.write.mode("append").insertInto("default.tweets")

        val users = df.select(
            "user.id",
            "user.name",
            "user.description",
            "user.followers_count",
            "user.location",
            "user.friends_count",
            "user.screen_name"
        )
        users.write.mode("append").insertInto("default.users")
    }
}