class TweetScanActorSpec extends TestKit(ActorSystem())
  with SpecificationLike with ImplicitSender {

  sequential

  val port = <our_port>
  def testQueryUrl(query: String) = s"http://localhost:$port/q=$query"

  val tweetScan = TestActorRef(new TweetScannerActor(testActor, testQueryUrl))

  "Getting all 'smack' tweets" >> {

    "should return more than 10 last entries" in {
      val twitterApi = TwitterApi(port)
      tweetScan ! "smack"
      Thread.sleep(1000)
      val tweets = expectMsgType[List[Tweet]]
      tweets.size mustEqual 10
      twitterApi.stop()
      success
    }
  }
}
