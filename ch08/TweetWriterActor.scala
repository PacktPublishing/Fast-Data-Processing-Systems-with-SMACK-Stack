
import scala.collection.JavaConversions._
import cassandra.resultset._
import context.dispatcher
import akka.pattern.pipe

class TweetWriterActor(cluster: Cluster) extends Actor {
  val session = cluster.connect(Keyspaces.akkaCassandra)
  val preparedStatement = session.prepare(
    "INSERT INTO tweets(key, user, text, creation_date) VALUES (?, ?, ?, ?);")

  def saveTweet(tweet: Tweet): Unit =
    session.executeAsync(preparedStatement.bind(
		tweet.id, tweet.user, tweet.text, tweet.creation_date))

  def receive: Receive = {
    case tweets: List[Tweet] => tweets foreach saveTweet
    case tweet:  Tweet       => saveTweet(tweet)
  }
}
