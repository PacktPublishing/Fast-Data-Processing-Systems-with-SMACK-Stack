object Main extends App with ConfigCassandraCluster {
  import Commands._
  import akka.actor.ActorDSL._

  def twitterSearchProxy(query: String) = 
    s"http://twitter-search-proxy.herokuapp.com/search/tweets?q=$query"

  implicit lazy val system = ActorSystem()
  val write = system.actorOf(Props(new TweetWriterActor(cluster)))
  val read = system.actorOf(Props(new TweetReaderActor(cluster)))
  val scan = system.actorOf(Props(new TweetScannerActor(write, twitterSearchProxy)))

  implicit val _ = actor(new Act {
    become {
      case x => println(">> " + x)
    }
  })

  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case QuitCommand            => return
      case ScanCommand(query)     => scan ! query.toString
      case ListCommand(count)     => read ! FindAll(count.toInt)
      case CountCommand           => read ! CountAll
      case _                      => return
    }

    commandLoop()
  }

  commandLoop()
  system.shutdown()

}
