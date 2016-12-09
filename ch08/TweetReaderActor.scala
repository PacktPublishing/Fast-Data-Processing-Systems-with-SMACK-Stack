
import scala.collection.JavaConversions._
import cassandra.resultset._
import context.dispatcher
import akka.pattern.pipe

class TweetReaderActor(cluster: Cluster) extends Actor {
  val session = cluster.connect(Keyspaces.akkaCassandra)
  val countAll = 
    new BoundStatement(session.prepare("select count(*) from tweets;"))


  def buildTweet(r: Row): Tweet = { //folded code...}

  def receive: Receive = {

    case FindAll(maximum)  =>
      val query = QueryBuilder.select().all().from(
		Keyspaces.akkaCassandra, "tweets").limit( maximum )
            session.executeAsync(query) 
        map(_.all().map(buildTweet).toList) pipeTo sender

    case CountAll =>
      session.executeAsync(countAll) 
        map(_.one.getLong(0)) pipeTo sender
  }
}
