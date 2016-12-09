// The first step is to import Spark using:

import org.apache.spark._

// Set the property spark.cassandra.connection.host to point to the address of one of the Cassandra nodes:

val conf = new SparkConf(true).set("spark.cassandra.connection.host", "CassandraNodeIP")

// To create the SparkContext, replace SparkMasterNodeIP with the Spark Master Address, use "local" to run in local mode:

val sc = new SparkContext("spark://SparkMasterNodeIP:7077", "test", conf)

// To enable the Cassandra specific functions on SparkContext, RDD, and DataFrame import:

import com.datastax.spark.connector._

// To load and analyze data from Cassandra use the sc.cassandraTable method to view a Cassandra table as a Spark RDD:

val testRDD = sc.cassandraTable("testKs", "kv")
println( testRDD.count )
println( testRDD.first )
println( testRDD.map(_.getInt("value")).sum )

// Here we save a RDD data to Cassandra, we will add two rows to the table:

val col = sc.parallelize(Seq(("key3", 3), ("key4", 4)))
col.saveToCassandra("testKs", "kv", SomeColumns("key", "value"))     
