
import com.tuplejump.calliope.utils.RichByteBuffer._
import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.CasBuilder

val cas = CasBuilder.thrift.withColumnFamily("calliopeDemo", "words")
val rdd = sc.thriftCassandra[String, Map[String, String]](cas)

val rdd = sc.thriftCassandra[String, Map[String, String]]("words", "calliopeDemo")

val rdd = sc.thriftCassandra[String, Map[String, String]] ("casserver.local", "9160", words", "calliopeDemo")
