package com.github.cloudsolutionhubs.tweetconsumer

/*
classes that define our data structures
for more info on case classes see
https://docs.scala-lang.org/tour/case-classes.html
*/

object classes {

    case class appConfig(
        broker: String,
        topic: String,
        stream: String = "",
        consumerKey: String = "",
        consumerSecret: String = "",
        accessToken: String = "",
        accessTokenSecret: String = ""
    )

    case class Tweet(
        day: String,
        tweet: String,
        city: String 
    )
    
    case class Survey(
        date: String,
        rating: String,
        city: String 
    )

  // TODO: finish implementing
  // see https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/intro-to-tweet-json#fundamentals
    case class RealTweet(created_at: String,
                          id_str: String,
                          text: String,
                          user: Object,
                         place: Object,
                         entities: Object,
                         extended_entities: Object
                        )


}