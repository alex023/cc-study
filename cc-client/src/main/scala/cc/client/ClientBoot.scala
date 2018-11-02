package cc.client
import com.typesafe.scalalogging.StrictLogging

object ClientBoot extends App with StrictLogging {
  import akka.actor.{ActorSystem, Props}
  import busines.{Client1, Client2}
  val system = ActorSystem("client")
  val client1 = system.actorOf(Props(new Client1), "client1")
  logger.info(s"client1 path= ${client1.path}")
  val client21 = system.actorOf(Props(new Client2("remotePlus")), "client2_1")
  logger.info(s"client2_remote path= ${client21.path}")
  val client22 = system.actorOf(Props(new Client2("localPlus")), "client2_2")
  logger.info(s"client2_local path= ${client22.path}")

}
