package actortests.parentchild

import akka.actor._

case class Hire(name: String)
case class Name(name: String)

class Employee extends Actor {
  var name = "John Doe"

  override def postStop {
    println(s"I'm ($name) and my Boss fired me: ${self.path}")
  }

  def receive = {
    case Name(name) => this.name = name
    case _ => println(s"Employee $name can't handle this message.")
  }
}

class Boss extends Actor {
  def receive = {
    case Hire(name) =>
      // here we hire personnel
      println(s"($name) is about to be hired")
      val employee = context.actorOf(Props[Employee], name = s"$name")
      employee ! Name(name)

    case _ => println(s"Boss can't handle this message.")
  }
}

object StartingDemo extends App {
  val actorSystem = ActorSystem("StartingDemo")
  val pointyHaired = actorSystem.actorOf(Props[Boss], name = "PointyHaired")

  // here the boss hires people
  pointyHaired ! Hire("Dilbert")
  pointyHaired ! Hire("Ted")

  // we wait some office cycles
  Thread.sleep(1000)

  // we look for Ted and we fire him
  println("Firing Ted ...")
  val ted = actorSystem.actorSelection("/user/PointyHaired/Ted")

  // PoisonPill is a special message in Akka
  ted ! PoisonPill
  println("now Ted is fired")

  actorSystem.shutdown
}