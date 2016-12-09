
package packt.ch05

import java.util
import java.util.Properties
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import kafka.consumer.ConsumerConfig
import MultiThreadConsumer._

import scala.collection.JavaConversions._

object MultiThreadConsumer {

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
    val threadCount = java.lang.Integer.parseInt(args(3))
    val multiThreadHLConsumer = new MultiThreadConsumer(zooKeeper, groupId, topic)
    multiThreadHLConsumer.testMultiThreadConsumer(threadCount)
    try {
      Thread.sleep(10000)
    } catch {
      case ie: InterruptedException =>
    }
    multiThreadHLConsumer.shutdown()
  }
}

class MultiThreadConsumer(zookeeper: String, groupId: String, topic: String) {

  private var executor: ExecutorService = _

  private val consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper,
    groupId))

  def shutdown() {
    if (consumer != null) consumer.shutdown()
    if (executor != null) executor.shutdown()
  }

  def testMultiThreadConsumer(threadCount: Int) {
    val topicMap = new util.HashMap[String, Integer]()

    // Define thread count for each topic
    topicMap.put(topic, threadCount)

    // Here we have used a single topic but we can also add
    // multiple topics to topicCount MAP
    val consumerStreamsMap = consumer.createMessageStreams(topicMap)
    val streamList = consumerStreamsMap.get(topic)

    // Launching the thread pool
    executor = Executors.newFixedThreadPool(threadCount)

    // Creating an object messages consumption
    var count = 0
    for (stream <- streamList) {
      val threadNumber = count
      executor.submit(new Runnable() {

        def run() {
          val consumerIte = stream.iterator()
          while (consumerIte.hasNext)
            println("Thread Number " + threadNumber + ": " + new String(consumerIte.next().message()))
          println("Shutting down Thread Number: " + threadNumber)
        }
      })
      count += 1
    }
    if (consumer != null) consumer.shutdown()
    if (executor != null) executor.shutdown()
  }
}
