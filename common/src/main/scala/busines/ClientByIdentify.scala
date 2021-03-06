package busines

import akka.actor.{Actor, ActorIdentity, ActorRef, Timers}
import cc.event.{Request, Response, Ticker}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.duration._
import scala.util.Random
/**
  * 基于Select Identify 方式获取远程对象，并进行通信
  */
class ClientByIdentify extends Actor with Timers with StrictLogging {


  val path = "akka://frontserver@127.0.0.1:2552/user/caculator"
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
    case ActorIdentity(id, actorRef) =>
      if (id == token && actorRef.isDefined) {
        context.become(stateActive(actorRef.get))
        actorRef foreach (_ ! newReq())
      } else {
        logger.debug(s"id=$id actor=$actorRef")
      }
  }

  def stateActive(remoteActor: ActorRef): Receive = {
    case Ticker =>
      remoteActor ! newReq()
    case Response(answer) =>
      logger.debug(s"answer is $answer")
  }

  def newReq(): Request = Request(Random.nextInt(20), Random.nextInt(10))
}

