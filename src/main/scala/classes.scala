package com.github.unofficialoraclecloudhub.streaming_data_analytics

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
    
    case class Tweet(
        date: String,
        text: String, 
        city: String 
    )
    
    case class Survey(
        date: String,
        rating: String,
        city: String 
    )
}