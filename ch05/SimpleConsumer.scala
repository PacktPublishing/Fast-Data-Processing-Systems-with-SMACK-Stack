package packt.ch05

import java.util
import java.util.Properties

import kafka.consumer.ConsumerConfig
import SimpleConsumer._

import scala.collection.JavaConversions._

object SimpleConsumer {

  private def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "500")
    props.put("zookeeper.sync.time.ms", "250")
    props.put("auto.commit.interval.ms", "1000")
    new ConsumerConfig(props)
  }

  def main(args: Array[String]) {
    val zooKeeper = args(0)
    val groupId = args(1)
    val topic = args(2)
    val simpleHLConsumer = new SimpleConsumer(zooKeeper, groupId, topic)
    simpleHLConsumer.testConsumer()
  }
}

class SimpleConsumer(zookeeper: String, groupId: String, private val topic: String) {

  private val consumer =
    kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId))

  def testConsumer() {
    val topicMap = new util.HashMap[String, Integer]()
    topicMap.put(topic, 1)
    val consumerStreamsMap = consumer.createMessageStreams(topicMap)
    val streamList = consumerStreamsMap.get(topic)
    for (stream <- streamList; aStream <- stream)
      println("Message from Single Topic :: " + new String(aStream.message()))
    if (consumer != null) {
      consumer.shutdown()
    }
  }
}
