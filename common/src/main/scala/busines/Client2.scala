package busines

import akka.actor.{Actor, Timers}
import com.typesafe.scalalogging.StrictLogging

class Client2(caculatorName: String)
    extends Actor
    with StrictLogging
    with Timers {
  import akka.actor.Props
  import cc.event.{Request, Response}

  import concurrent.duration._
  import scala.util.Random
  val actorRef = context.system.actorOf(Props(new Caculator), caculatorName)
  override def preStart(): Unit = {
    timers.startPeriodicTimer("client2", Ticker, 1.seconds)
  }
  override def receive: Receive = {
    case Ticker =>
      actorRef ! newReq()
    case Response(answer) =>
      logger.debug(s"$caculatorName receive $answer")
  }
  def newReq(): Request = Request(Random.nextInt(20), Random.nextInt(10))

}
