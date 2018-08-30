package com.github.unofficialoraclecloudhub.streaming_data_analytics

import org.apache.spark.sql.{SparkSession}
import _root_.kafka.serializer.StringDecoder //http://stackoverflow.com/questions/36397688/sbt-cannot-import-kafka-encoder-decoder-classes
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.log4j.{Level, LogManager, PropertyConfigurator}


object main {
    def main(args: Array[String]){
        @transient lazy val log = LogManager.getLogger("myLogger")
        
        val brokers = args(0)
        val topic = args(1)

        // stream tweets or surveys
        var streamSurvey = true
        try {
            val streamName = args(2)
            streamSurvey = streamName.contains("survey")
        } catch {
            case e: Exception =>
                println("something went wrong getting the stream, defaulting to tweets")
        }

        log.info("streaming survey: " + streamSurvey)

        val spark = SparkSession
            .builder()
            .appName("Streaming Tweets")
            .config("master", "yarn")
            .enableHiveSupport()
            .getOrCreate()
        
        import spark.implicits._

        util.createTables(spark)

        val ssc = new StreamingContext(spark.sparkContext, Seconds(5))

        //https://spark.apache.org/docs/1.6.1/streaming-kafka-integration.html
        val topicsSet = topic.split(",").toSet
        val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
        val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

        //for debugging, you can print the full contents of the first 10 rows of each batch of messages by uncommenting the following
        messages.print()

        messages.foreachRDD(rdd => {
            var df=spark.read.json(rdd.map(x => x._2))

           // debug 
        //    df.show()
        //    println(df.getClass.getSimpleName)
           
            if(streamSurvey){
                var ds = df.as[classes.Survey]
                util.handleSurvey(ds)

            } else {
                var ds = df.as[classes.Tweet]
                util.handleTweet(ds)

            }
        })

        println("Starting Streaming Context")
        ssc.start()
        ssc.awaitTermination()
    }
}