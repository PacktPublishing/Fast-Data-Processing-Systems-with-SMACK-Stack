// Import
import com.datastax.spark.connector.types.CassandraOption

// Setup some samlpe data (1, 1, 1) ... (6, 6, 6)
sc.parallelize(1 to 6).map(x => (x,x,x)).saveToCassandra(testKs, "table1")

// To setup Scala options Rdd (1, None, None) (2, None, None) ... (6, None, None)
val optionRdd = sc.parallelize(1 to 6).map(x => (x, None, None))

// To delete just middle column
optionRdd.map{ case (x: Int, y: Option[Int], z: Option[Int]) => (x, CassandraOption.deleteIfNone(y), CassandraOption.unsetIfNone(z))}.saveToCassandra(testKs, "tab1")

// To collect the result:
val results = sc.cassandraTable[(Int, Option[Int], Option[Int])](ks, "tab1").collect
