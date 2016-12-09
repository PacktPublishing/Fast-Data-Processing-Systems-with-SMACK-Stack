import com.tuplejump.calliope.Implicits._
import com.tuplejump.calliope.CasBuilder
import com.tuplejump.calliope.utils.RichByteBuffer._

val cas = CasBuilder.cql3.withColumnFamily("calliopeDemo", "Words")
            .saveWithQuery(
		"UPDATE calliopeDemo.words set book_name = ?, book_content = ?")

rdd.cql3SaveToCassandra(cas)
