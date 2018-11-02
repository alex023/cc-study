package cc.front
import com.typesafe.scalalogging.StrictLogging

object FrontBoot extends App with StrictLogging {
  import akka.actor.{ActorSystem, Props}
  import busines.Caculator
  val system = ActorSystem("frontserver")
  val frontActor = system.actorOf(Props(new Caculator), "caculator")
  logger.info(s"actor path= ${frontActor.path}")
}
