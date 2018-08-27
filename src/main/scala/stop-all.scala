package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.streaming._

object App{
    def stopStreaming() {
        println("Stopping any active StreamingContext.  May take a minute.")
        StreamingContext.getActive().map(_.stop(false,true))
        println("done")
    }
}
