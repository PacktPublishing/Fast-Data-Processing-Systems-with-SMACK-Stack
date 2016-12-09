
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class DroneActor extends Actor {
  def receive = {
    case "left"  => println("turning left")
    case "right" => println("turning right")
    case "up"    => println("ascending")
    case "down"  => println("descending")
    case _       => println("command not recognized")
  }
}

object Main extends App {

  // always build the ActorSystem
  val actorSystem = ActorSystem("DroneSystem")

  // create the actor
  val parrot = actorSystem.actorOf(Props[DroneActor], name = "droneactor")

  // send the actor some messages
  parrot ! "up"
  parrot ! "right"
  parrot ! "drop the bombs"


  // shut down the actor system
  actorSystem.shutdown
}

