package com.github.unofficialoraclecloudhub.streaming_data_analytics

import _root_.kafka.serializer.StringDecoder //http://stackoverflow.com/questions/36397688/sbt-cannot-import-kafka-encoder-decoder-classes
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._


object App {
    def main(args: Args[String]){
        println("Creating new Streaming Context")
        val ssc = new StreamingContext(sc, Seconds(5))

        val topic = z.angular("BIND_OEHCS_Topic").toString
        println("topic:"+topic)
        val topicsSet = topic.split(",").toSet

        val brokers=z.angular("BIND_OEHCS_ConnectionDescriptor").toString
        println("brokers:"+brokers)
        val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)

        println("Creating Kafka DStream")
        //https://spark.apache.org/docs/1.6.1/streaming-kafka-integration.html
        val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
        println("Setting up operations on DStream")    
        //for debugging, you can print the full contents of the first 10 rows of each batch of messages by uncommenting the following
        //messages.print()

        messages.foreachRDD(rdd => {
           var df=sqlContext.read.json(rdd.map(x => x._2))
           df.show()
           // var df=sqlContext.createDataFrame(rdd)
           println(df.getClass.getSimpleName)

        })

        println("Starting Streaming Context")
        ssc.start()
        println("Will now sleep for a few minutes, before stopping the StreamingContext.  At this point, you should start the producer.")
        Thread.sleep(90)//000)  //now sleep for 1.5 minutes.  Parameter is milliseconds
        //stop any active streamingcontexts.  Parameters are boolean stopSparkContext, boolean stopGracefully
        println("Stopping Active StreamingContext")
        StreamingContext.getActive().map(_.stop(false,true))
        println("done")
    }
}