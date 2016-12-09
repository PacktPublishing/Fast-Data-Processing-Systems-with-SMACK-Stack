
import com.datastax.spark.connector.streaming._
counts.saveToCassandra("streamingTest", "words")

    val scontext = new StreamingContext(conf, Seconds(n))

import com.datastax.spark.connector.streaming.TypedStreamingActor
val kafkaStream = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder]( scontext, kafka.kafkaParams, Map(topic -> 1), StorageLevel.MEMORY_ONLY)
val akkaStream = scontext.actorStream[String](Props[TypedStreamingActor[String]], "stream", StorageLevel.MEMORY_AND_DISK)

import com.datastax.spark.connector.streaming._

val wc = stream.flatMap(_.split("\\s+"))
	.map(x => (x, 1))
	.reduceByKey(_ + _)
	.saveToCassandra("testKs", "words", SomeColumns("word", "count")) 

scontext.start()

val col = sc.parallelize(Seq(("key3", 3), ("key4", 4)))
col.saveToCassandra("testKs", "kv", SomeColumns("key", "value"))       
Read the Stream from Cassandra
To read the Streaming Context from Cassandra:
val rdd = ssc.cassandraTable("testKs", "key_value")
	.select("key", "value").where("foo = ?", 3)

val rdd = sc.cassandraTable("testKs", "key_value")
println( rdd.count )
println( rdd.first )
println( rdd.map(_.getInt("value")).sum )
