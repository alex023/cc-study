package cc.event

sealed trait Event
case class Request(x: Int, y: Int) extends Event
case class Response(plus: Int) extends Event
case object Ticker
