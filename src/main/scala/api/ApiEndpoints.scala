package userlogin.api

import zio.*
import zio.http.*

case class ApiEndpoints private () {

  def endpoints
    : Routes[Any, Response]
    = Routes(
      Method.GET / "api" -> handler(Response.text("Hello World API")),
    )
}

object ApiEndpoints {
  val live = ZLayer.derive[ApiEndpoints]
}
