package com.github.cloudsolutionhubs.tweetconsumer

import org.apache.spark.streaming._

object App{
    def stopStreaming() {
        println("Stopping any active StreamingContext.  May take a minute.")
        StreamingContext.getActive().map(_.stop(false,true))
        println("done")
    }
}
