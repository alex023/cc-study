package cc.client
import akka.actor.{ActorSystem, Props}
import busines.{ClientByIdentify, ClientCreateRemote}
import com.typesafe.scalalogging.StrictLogging

object ClientBoot extends App with StrictLogging {
  val system = ActorSystem("client")
  //测试远程查询
  val client1 = system.actorOf(Props(new ClientByIdentify), "client1")
  logger.info(s"client1 path= ${client1.path}")

  //测试远程调用对象查询及创建
  val client21 =
    system.actorOf(
      Props(new ClientCreateRemote("client21", "remotePlus")),
      "client2_1"
    )
  logger.info(s"client2_remote path= ${client21.path}")
  Thread.sleep(10000)
  val client22 =
    system.actorOf(
      Props(new ClientCreateRemote("client22", "remotePlus")),
      "client2_2"
    )
  logger.info(s"client2_remote path= ${client22.path}")
}
