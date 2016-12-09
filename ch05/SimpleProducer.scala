package packt.ch05

import java.util.{Date, Properties}

import packt.ch05.SimpleProducer._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object SimpleProducer {

  private var producer: KafkaProducer[String, String] = _

  def main(args: Array[String]) {
    val argsCount = args.length
    if (argsCount == 0 || argsCount == 1)
      throw new IllegalArgumentException(
        "Provide topic name and Message count as arguments")

    // Topic name and the message count to be published is passed from the
    // command line
    val topic = args(0)
    val count = args(1)

    val messageCount = java.lang.Integer.parseInt(count)
    println("Topic Name - " + topic)
    println("Message Count - " + messageCount)
    val simpleProducer = new SimpleProducer()
    simpleProducer.publishMessage(topic, messageCount)
  }
}

class SimpleProducer {

  val props = new Properties()

  // Set the broker list for requesting metadata to find the lead broker
  props.put("metadata.broker.list",
    "192.168.146.132:9092, 192.168.146.132:9093, 192.168 146.132:9094 ")

  //This specifies the serializer class for keys
  props.put("serializer.class", "kafka.serializer.StringEncoder")

  // 1 means the producer receives an acknowledgment once the lead replica
  // has received the data. This option provides better durability as the
  // client waits until the server acknowledges the request as successful.
  props.put("request.required.acks", "1")

  producer = new KafkaProducer(props)

  private def publishMessage(topic: String, messageCount: Int) {
    for (mCount <- 0 until messageCount) {
      val runtime = new Date().toString
      val msg = "Message Publishing Time - " + runtime
      println(msg)

      // Create a message
      val data = new ProducerRecord[String, String](topic, msg)

      // Publish the message
      producer.send(data)
    }

    // Close producer connection with broker.
    producer.close()
  }
}
