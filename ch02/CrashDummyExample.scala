import akka.actor._

class CrashDummy extends Actor {
	def receive = {
		case s:String => println("Message received: " + s)
		case _ => println("Unknown message received")
	}
	override def postStop { println("CrashDummy's Will")
}

object CrashDummyExample1 extends App {
	val system = ActorSystem("CrashDummyExample")
	val dummy = system.actorOf(Props[CrashDummy], name = "dummy")

	// a message
	dummy ! "open your mouth"
	
	// take the poison
	dummy ! PoisonPill

	// dummy is stopped
	dummy ! "OMG what I've done!"
	
	// stop our crash dummy
	system.shutdown
}

object CrashDummyExample2 extends App {
	val system = ActorSystem("CrashDummyExample")
	val dummy = system.actorOf(Props[CrashDummy], name = "dummy")

	// a message
	dummy ! "open your mouth"
	
	// take the poison
	dummy ! PoisonPill

	// dummy is stopped
	dummy ! "OMG what I've done!"
	
	// stop our crash dummy
	system.shutdown
}

object CrashDummyExample3 extends App {
	val system = ActorSystem("CrashDummyExample")
	val dummy = system.actorOf(Props[CrashDummy], name = "dummy")
	
	// try to stop the dummy gracefully
	try {
		val stopped: Future[Boolean] = gracefulStop(dummy, 2 			seconds)(system)
		Await.result(stopped, 3 seconds)
		println("dummy was stopped")
	} catch {
		case e:Exception => e.printStackTrace
	} finally {
		system.shutdown
	}
}
