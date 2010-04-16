package nugoh.service.scalaTest

import java.util.Map
import actors.Actor
import actors.Actor._

class Echo() extends Actor {
  def act() {
    loop {
      react {
        case _ => "hello scala " + this
      }
    }
  }
}



case class ScalaTest() {
  var actors = List[Echo]()


  def init() = {
    for (i <- 0 until 100000) {
      val e = new Echo()
      e.start()
      actors = e :: actors
    }
  }

  def run(context: Map[String, Object]) = {
	println("RUN")
    actors.foreach(e => e ! "tick")
  }
}