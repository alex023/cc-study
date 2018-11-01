package cc.client
import akka.actor.{Actor, Timers}
import com.typesafe.scalalogging.StrictLogging
import concurrent.duration._
import scala.util.Random

class Client extends Actor with Timers with StrictLogging {
  import akka.actor.{ActorIdentity, ActorRef}
  import cc.event.{Request, Response}
  import cc.client.Client.Ticker

  val path = "akka.tcp://frontserver@127.0.0.1:2552/user/front"
  val id = Random.nextInt()
  val selection = context.actorSelection(path)
  var count = 0
  override def preStart(): Unit = {
    import akka.actor.Identify
    timers.startPeriodicTimer("client", Ticker, 1.second)
    selection ! Identify(id)
  }

  override def postStop(): Unit = {
    timers.cancelAll()
  }

  override def receive: Receive = stateInt(id)
  def stateInt(token: Int): Receive = {
    case ActorIdentity(id, remoteActor) =>
      if (id == token && remoteActor.isDefined) {
        remoteActor.get ! newReq()
        context.become(stateActive(remoteActor.get))
      } else {
        logger.debug(s"id=$id actor=$remoteActor")
      }
  }

  def stateActive(remoteActor: ActorRef): Receive = {
    case Ticker =>
      logger.debug(s"count=$count")
      count = 0
    case Response(_) =>
      count += 1
      remoteActor ! newReq()
  }
  def newReq(): Request = Request(Random.nextInt(20), Random.nextInt(10))
}

object Client {
  object Ticker
}
