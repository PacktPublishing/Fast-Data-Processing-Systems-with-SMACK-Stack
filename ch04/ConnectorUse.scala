import com.datastax.spark.connector._, 
org.apache.spark.SparkContext, 
org.apache.spark.SparkContext._, 
org.apache.spark.SparkConf 

// We set a variable with the required configuration to connect.
val conf = new SparkConf(true).set("spark.cassandra.connection.host", "localhost")

// And finally we connect to our well known Key Space and our table, 
// both created at the beginning of this chapter.
val sc = new SparkContext(conf)
val test_spark_rdd = sc.cassandraTable("mykeyspace", "cars")

// Given the context and Key Space is possible to consult the values with the following statement.
test_spark_rdd.foreach(println)

