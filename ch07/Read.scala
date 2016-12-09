import com.tuplejump.calliope.utils.RichByteBuffer._
import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.CasBuilder

val cas = CasBuilder.cql3.withColumnFamily("calliopeDemo", "words")
val rdd = sc.cql3Cassandra[Map[String, String], Map[String, String]](cas)

val rdd = sc.cql3Cassandra[Map[String, String], Map[String, String]]("words", "calliopeDemo")

val rdd = sc.cql3Cassandra[Map[String, String], Map[String, String]] ("casserver.local", "9160", Words", "calliopeDemo")

val cas = CasBuilder.cql3.withColumnFamily("calliopeDemo", "Words")
                                .where("book = 'Learning SMACK'")

val rdd = sc.cql3Cassandra[Map[String, String], Map[String, String]](cas)



