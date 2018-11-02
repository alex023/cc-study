package cc.front
import com.typesafe.scalalogging.StrictLogging
import akka.actor.{ActorSystem, Props}
import busines.Caculator

object FrontBoot extends App with StrictLogging {
  val system = ActorSystem("frontserver")
  val frontActor = system.actorOf(Props(new Caculator), "caculator")
  logger.info(s"actor path= ${frontActor.path}")
}
