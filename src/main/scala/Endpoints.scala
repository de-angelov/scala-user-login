package userlogin.endpoints

import userlogin.api.{ApiEndpoints}
import userlogin.pages.{PagesEndpoints}

import zio._
import zio.http._


case class Endpoints private (
  api: ApiEndpoints,
  pages: PagesEndpoints
) {


  def notFound
    : Routes[Any, Response]
    =  Routes(
      Method.ANY / trailing -> handler { Response.text("Page does not exist")}
    )

  def endpoints
      : Routes[Any, Response]
      = pages.endpoints ++ api.endpoints  ++ notFound
}

object Endpoints {
  val live = ZLayer.derive[Endpoints]
}

