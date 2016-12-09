import akka.actor._

class Convict extends Actor {
	def receive = {
		case _ => println("Convict received a message")
	}

	override def preStart { println("In Convict preStart") }
	override def postStop { println("In Convict postStop") }
	override def preRestart(reason: Throwable, message: 		Option[Any]) {
		println("In Convict preRestart")
	}
	override def postRestart(reason: Throwable) {
		println("In Convict postRestart")
	}
}

object GreenMile extends App {
	val system = ActorSystem("GreenMile")
	val con = system.actorOf(Props[Convict], name = "Convict")

	con ! "last words"
	
	// send him towards the light
	con ! Kill
	
	system.shutdown
}
