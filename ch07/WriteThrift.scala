
import com.tuplejump.calliope.utils.RichByteBuffer._
import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.CasBuilder

val cas = CasBuilder.thrift.withColumnFamily("calliopeDemo", "words")
val rdd = sc.thriftCassandra[String, Map[String, String]](cas)

So here we don't need to customize any of the advanced options for Cassandra connection, we can use the simplified API as follows:

val rdd = sc.thriftCassandra[String, Map[String, String]]("words", "calliopeDemo")

If Cassandra is not running on the same host as the SparkContext, use the following:

val rdd = sc.thriftCassandra[String, Map[String, String]] ("casserver.local", "9160", words", "calliopeDemo")
