package userlogin.pages

import userlogin.helpers.{ bearerAuthWithContext }

import zio._
import zio.http._
import userlogin.types.{AppConfig}
import userlogin.pages.login.renderLoginPage
import userlogin.pages.home.renderHomePage
import userlogin.pages.secret.renderSecretPage


case class PagesEndpoints private(config: AppConfig) {

    val loginPage = Method.GET / "login" -> handler(renderLoginPage)

    val secretPage = Method.GET / "secret" -> handler({
      (req: Request) =>
      val username = "todo"
      renderSecretPage(username)
    }) @@ bearerAuthWithContext(config.jwtString)

    val homePage = Method.GET / "" -> handler(renderHomePage)



    def endpoints
    : Routes[Any, Response]
    = Routes(
      homePage,
      secretPage ,
      loginPage,
    )
}


object PagesEndpoints:
    val live = ZLayer.derive[PagesEndpoints]
