package busines

import akka.actor.{Actor, ActorRef, ActorSelection, Timers}
import cc.event.{Request, Response, Ticker}
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Random, Success}

/**
  * 基于selection获取远程对象，如果不存在则创建
  *
  * @param id
  * @param caculatorName
  */
class ClientCreateRemote(id: String, caculatorName: String)
    extends Actor
    with StrictLogging
    with Timers {

  var actorRef: Option[ActorRef] = None

  override def preStart(): Unit = {
    //val path = "akka://frontserver@127.0.0.1:2552/remote/akka/client@127.0.0.1:2553/user/"
    //即使远程创建爱你，只需要关心当前节点地址，不需要写上一行的完整路径
    //但要注意，子actor的创建方式为 context.actorOf，还是system.actorOf
    import context.dispatcher

    import concurrent.duration._
    val path = s"/user/"
    val selection: ActorSelection =
      context.actorSelection(path + caculatorName)
    selection.resolveOne(5.seconds).onComplete {
      case Success(resolved) =>
        actorRef = Some(resolved)
      case Failure(_) =>
        import akka.actor.Props
        actorRef =
          Some(context.system.actorOf(Props(new Caculator), caculatorName))
    }
    timers.startPeriodicTimer(id, Ticker, 2.seconds)
  }

  override def postStop(): Unit = timers.cancelAll()

  override def receive: Receive = {
    case Ticker =>
      actorRef foreach (_ ! newReq())
    case Response(answer) =>
      logger.debug(s"$id receive $answer")
  }

  def newReq(): Request = Request(Random.nextInt(20), Random.nextInt(10))
}
