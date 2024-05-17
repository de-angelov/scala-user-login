package userlogin.api

import userlogin.db.{RepositoryService}
import userlogin.types.{UserPassword}

import zio.*
import zio.http.*
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.chaining.scalaUtilChainingOps

case class ApiEndpoints private (dbr: RepositoryService) {

  private val register
    = Method.POST / "api" / "register" -> handler {
    (req: Request) =>

    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)


    Response.text("handleRegister")
  }

  private val logout
    = Method.POST / "api" / "logout" -> handler {
    (req: Request) =>

    Response.text("handleLogout")
  }

  private val login
    = Method.POST / "api" / "login" -> handler {
    (req: Request) =>

    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)


    val result = for {
      username
        <- form
        .map(_.get("username"))
        .flatMap(ff => ZIO.fromOption(ff).orElseFail(Response.badRequest("Missing username field!")))
        .flatMap(ff => ZIO.fromOption(ff.stringValue).orElseFail(Response.badRequest("Missing username value!")))

      password
        <- form
        .map(_.get("password"))
        .flatMap(ff => ZIO.fromOption(ff).orElseFail(Response.badRequest("Missing password field!")))
        .flatMap(ff => ZIO.fromOption(ff.stringValue).orElseFail(Response.badRequest("Missing password value!")))

      // password
      //   <- form
      //   .map(_.get("password"))
      //   .flatMap{ case Some(x) => x.stringValue.pipe(ZIO.succeed) }
      //   .map(ZIO.fromOption)
      //   .flatMap(_.orElseFail(Response.notFound))

      user <-
          dbr
          .getUser(username, UserPassword.apply(password))
          .pipe(ZIO.fromOption)
          .orElseFail(Response.notFound)

     } yield  user

     result
  }



  def endpoints
    : Routes[Any, Response]
    = Routes(
      logout,
      login,
      register
    )
}

object ApiEndpoints {
  val live = ZLayer.derive[ApiEndpoints]
}
