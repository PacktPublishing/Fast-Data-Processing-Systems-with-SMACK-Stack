import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.softwaremill.react.kafka.KafkaMessages._
import org.apache.kafka.common.serialization.{StringSerializer, StringDeserializer}
import com.softwaremill.react.kafka.{ProducerMessage, ConsumerProperties, ProducerProperties, ReactiveKafka}
import org.reactivestreams.{ Publisher, Subscriber }

implicit val actorSystem = ActorSystem("ReactiveKafka")
implicit val materializer = ActorMaterializer()

val kafka = new ReactiveKafka()
val publisher: Publisher[StringConsumerRecord] = kafka.consume(ConsumerProperties(
 bootstrapServers = "localhost:9092",
 topic = "lowercaseStrings",
 groupId = "groupName",
 valueDeserializer = new StringDeserializer()
))

val subscriber: Subscriber[StringProducerMessage] = kafka.publish(ProducerProperties(
  bootstrapServers = "localhost:9092",
  topic = "uppercaseStrings",
  valueSerializer = new StringSerializer()
))

Source.fromPublisher(publisher).map(m => ProducerMessage(m.value().toUpperCase))
  .to(Sink.fromSubscriber(subscriber)).run()
