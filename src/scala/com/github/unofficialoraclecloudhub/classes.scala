package com.github.unofficialoraclecloudhub.streaming_data_analytics

object App {
    case class User(
        id: Long, 
        name: String,
        description: String, 
        followers_count: Long, 
        location: String,
        friends_count: Long, 
        screen_name: String
    )
    
    
    case class Tweet(
        id: Long,
        created_at: String,         
        text: String,
        favorite_count: Int,
        retweet_count: Int,
        quote_count: Int,
        reply_count: Int,
        lang: String,
        coordinates: String,
        place: String,
        possibly_sensitive: String,
        user: User
    )
}