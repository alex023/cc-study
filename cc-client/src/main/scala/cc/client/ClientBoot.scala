package cc.client
import com.typesafe.scalalogging.StrictLogging

object ClientBoot extends App with StrictLogging {
  import akka.actor.{ActorSystem, Props}
  val system = ActorSystem("client")
  val clientActor = system.actorOf(Props(new Client), "agent")
  logger.info(s"actor path= ${ clientActor.path}")
}
