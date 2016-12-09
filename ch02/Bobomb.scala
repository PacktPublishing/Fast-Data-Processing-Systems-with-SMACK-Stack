import akka.actor._

class Bobomb extends Actor {

  println("in the Bobomb constructor")

  override def preStart {
    println("bobomb: preStart")
  }

  override def postStop {
    println("bobomb: postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("in bobomb preRestart")
    println(s" MESSAGE: ${message.getOrElse("")}")
    println(s" REASON: ${reason.getMessage}")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) {
    println("in bobomb postRestart")
    println(s" REASON: ${reason.getMessage}")
    super.postRestart(reason)
  }

  def receive = {
    case ForceRestart => throw new Exception("Boom!")
    case _ => println("Bobomb received a message")
  }
}

case object ForceRestart

object LifecycleDemo extends App {
  val system = ActorSystem("BombSystem")
  val bobomb = system.actorOf(Props[Bobomb], name = "Bobomb")
  println("sending bobomb a message")
  bobomb ! "activate"
  Thread.sleep(4000)
  println("making bobomb exploit")
  bobomb ! ForceRestart
  Thread.sleep(4000)
  println("stopping bobomb")
  system.stop(bobomb)
  println("shutting down system")
  system.shutdown
}