package userlogin.api

import userlogin.db.{RepositoryService}
import userlogin.types.{UserPassword, AppConfig}
import userlogin.helpers.{jwtEncode}


import zio.*
import zio.http.*
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.chaining.scalaUtilChainingOps
import userlogin.types.HashedPassword

case class ApiEndpoints private (dbr: RepositoryService, config: AppConfig) {

private val extractField
  : ( ZIO[Any, Response, Form], String) => ZIO[Any, Response, String]
  = (form, fieldName) =>
    for {
      fieldOpt
        <- form
        .map(_.get(fieldName))
        .flatMap(ZIO.fromOption)
        .orElseFail(Response.badRequest(s"Missing $fieldName field!"))

      fieldValue
        <- fieldOpt
        .stringValue
        .pipe(ZIO.fromOption)
        .orElseFail(Response.badRequest(s"Missing $fieldName value!"))

    } yield fieldValue




  private val register
    = Method.POST / "api" / "register" -> handler {
    (req: Request) =>

    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)

    for {
      username <- extractField(form, "username")
      password <- extractField(form, "password")
      result <- dbr.saveNewUser(username, UserPassword.apply(password))
    } yield result match
      case Some(value) => Response.text(jwtEncode(username, config.jwtString))
      case _ => Response.unauthorized("Invalid username or password.")
  }

  private val logout
    = Method.POST / "api" / "logout" -> handler {
    (req: Request) =>
    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)

    for {
      username <- extractField(form, "username")
      password <- extractField(form, "password")
      result <- dbr.getUser(username, HashedPassword.apply(password))
    } yield result match
      case Some(value) => Response.text(jwtEncode(username, config.jwtString, -1))
      case None => Response.unauthorized("Invalid username or password.")
  }

  private val login
    = Method.POST / "api" / "login" -> handler {
    (req: Request) =>

    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)


    val result = for {
      username <- extractField(form, "username")
      password <- extractField(form, "password")

      userOpt
        <- dbr
        .getUser(username, HashedPassword.apply(password))
        // .pipe(ZIO.succeed)

      finalResult <- userOpt match {
        case Some(_) => ZIO.succeed(Response.text(jwtEncode(username, config.jwtString)))
        case _ => ZIO.fail(Response.unauthorized("Invalid username or password."))
      }

    } yield finalResult

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
