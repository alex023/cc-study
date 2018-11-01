package cc.front
import akka.actor.Actor
import com.typesafe.scalalogging.StrictLogging

class FrontActor extends Actor with StrictLogging {
  import cc.event.{Request, Response}
  case class Item(x: Int, y: Int, result: Int)
  override def receive: Receive = {
    case Request(x, y) =>
      sender() ! Response(x + y)
  }
}
