package userlogin.endpoints

import userlogin.api.{ApiEndpoints}
import userlogin.pages.{PagesEndpoints}

import zio.*
import zio.http.*

case class Endpoints private (
  api: ApiEndpoints,
  pages: PagesEndpoints
) {

  def endpoints
      : Routes[Any, Response]
      = pages.endpoints ++ api.endpoints
}

object Endpoints {
  val live = ZLayer.derive[Endpoints]
}
