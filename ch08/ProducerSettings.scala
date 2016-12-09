import akka.kafka._
import akka.kafka.scaladsl._
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.ByteArraySerializer

val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer).withBootstrapServers("localhost:9092")

Source(1 to 10000)
  .map(_.toString)
  .map(elem => new ProducerRecord[Array[Byte], String]("topic1", elem))
  .to(Producer.plainSink(producerSettings))

Source(1 to 10000).map(elem => ProducerMessage.Message(new ProducerRecord[Array[Byte], String]("topic1", elem.toString), elem))
    .via(Producer.flow(producerSettings))
    .map { result =>
      val record = result.message.record
      println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value} (${result.message.passThrough}")
      result
    }
