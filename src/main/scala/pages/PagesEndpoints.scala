package userlogin.pages


import zio.*
import zio.http.*

case class PagesEndpoints private() {

    val loginPage = Method.GET / "login" -> handler(renderLoginPage)

    val secretPage = Method.GET / "secret" -> handler(renderSecretPage)

    val homePage = Method.GET / "" -> handler(renderHomePage)

    def endpoints
    : Routes[Any, Response]
    = Routes(
      homePage,
      secretPage,
      loginPage,
    )
}


object PagesEndpoints:
    val live = ZLayer.derive[PagesEndpoints]
