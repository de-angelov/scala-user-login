package userlogin.pages


import zio.*
import zio.http.*

case class PagesEndpoints private() {
    def endpoints
    : Routes[Any, Response]
    = Routes(
      Method.GET / "" -> handler(Response.text("Hello World")),
    )
}


object PagesEndpoints:
    val live = ZLayer.derive[PagesEndpoints]
