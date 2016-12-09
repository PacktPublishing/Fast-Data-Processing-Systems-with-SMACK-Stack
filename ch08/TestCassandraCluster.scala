
import scala.collection.JavaConversions._

trait TestCassandraCluster extends CassandraCluster {
  def system: ActorSystem

  private def config = system.settings.config

  private val cassandraConfig = 
    config.getConfig("akka-cassandra.test.db.cassandra")
  private val port = cassandraConfig.getInt("port")
  private val hosts = cassandraConfig.getStringList("hosts").toList

  lazy val cluster: Cluster =
    Cluster.builder().
      addContactPoints(hosts: _*).
      withPort(port).
      withCompression(ProtocolOptions.Compression.SNAPPY).
      build()
}
