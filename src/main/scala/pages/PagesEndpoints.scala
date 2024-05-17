package userlogin.pages


import zio.*
import zio.http.*

case class PagesEndpoints private() {

    val LoginPage = ???

    val SecretPage = ???

    val HomePage = ???

    def endpoints
    : Routes[Any, Response]
    = Routes(
      HomePage,
      LoginPage,
      SecretPage,
      Method.GET / "" -> handler(Response.text("Hello World")),
    )
}


object PagesEndpoints:
    val live = ZLayer.derive[PagesEndpoints]
