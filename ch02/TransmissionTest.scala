
import akka.actor._

case object SendPackageMessage
case object AcknowledgeMessage
case object StartTransmissionMessage
case object StopTransmissionMessage

class Transmitter(receiver: ActorRef) extends Actor {
  val messages = 100
  var count = 0

  def increment {
    count += 1;
    println("message sent")
  }

  def receive = {
    case StartTransmissionMessage =>
      increment
      receiver ! SendPackageMessage
    case AcknowledgeMessage =>
      increment
      if (count >= messages) {
        sender ! StopTransmissionMessage
        println("transmitter stopped")
        context.stop(self)
      } else {
        sender ! SendPackageMessage
      }
    case _ => println("Transmitter: message not recognized")
  }
}

class Receiver extends Actor {
  def receive = {
    case SendPackageMessage =>
      println(" received")
      sender ! AcknowledgeMessage
    case StopTransmissionMessage =>
      println("Receiver stopped")
      context.stop(self)
    case _ => println("Receiver: message not recognized")
  }
}

object TransmissionTest extends App {
  val system = ActorSystem("TransmissionSystem")
  val receiver = system.actorOf(Props[Receiver], name = "receiver")
  val transmitter = system.actorOf(Props(new Transmitter(receiver)), name = "transmitter")

  // start the transmission
  transmitter ! StartMessage

  //system.shutdown
}
