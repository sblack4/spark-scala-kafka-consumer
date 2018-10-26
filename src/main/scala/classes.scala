package com.github.cloudsolutionhubs.tweetconsumer

/*
  ## Avocado Text 
  - datetime
  - text (tweets but actually alice in wonderland)
  - City
  
  ## Avocado Survey 
  - datetime
  - rating (0-3)
  - City
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

  // TODO: implement
    case class RealTweet()


}