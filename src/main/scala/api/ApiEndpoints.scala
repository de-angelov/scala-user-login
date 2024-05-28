package userlogin.api

import userlogin.db.{RepositoryService}
import userlogin.types.{UserPassword, AppConfig}
import userlogin.helpers.{jwtEncode}


import zio.*
import zio.http.*
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.chaining.scalaUtilChainingOps

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
    } yield dbr.saveNewUser(username, UserPassword.apply(password)) match
      case Some(value) => Response.text(jwtEncode(username, config.jwtString))
      case _ => Response.unauthorized("Invalid username or password.")
  }

  private val logout
    = Method.POST / "api" / "logout" -> handler {
    (req: Request) =>
    val form= req.body.asMultipartForm.orElseFail(Response.badRequest)= req.body.asMultipartForm.orElseFail(Response.badRequest) = req.body.asMultipartForm.orElseFail(Response.badRequest)

    for {
      username <- extractField(form, "username")
      password <- extractField(form, "password")
    } yield dbr.saveNewUser(username, UserPassword.apply(password)) match
      case Some(value) => Response.text(jwtEncode(username, config.jwtString, -1))
      case _ => Response.unauthorized("Invalid username or password.")
      // case None => Response.unauthorized("Invalid username or password.")
  }

  private val login
    = Method.POST / "api" / "login" -> handler {
    (req: Request) =>

    val form = req.body.asMultipartForm.orElseFail(Response.badRequest)


    val result = for {
      username <- extractField(form, "username")
      password <- extractField(form, "password")


        // <- form
        // .map(_.get("password"))
        // .flatMap(ff => ZIO.fromOption(ff).orElseFail(Response.badRequest("Missing password field!")))
        // .flatMap(ff => ZIO.fromOption(ff.stringValue).orElseFail(Response.badRequest("Missing password value!")))

      // password
      //   <- form
      //   .map(_.get("password"))
      //   .flatMap{ case Some(x) => x.stringValue.pipe(ZIO.succeed) }
      //   .map(ZIO.fromOption)
      //   .flatMap(_.orElseFail(Response.notFound))

      // user <-
      //     dbr
      //     .getUser(username, UserPassword.apply(password))
          // .pipe(ZIO.fromOption)
          // .orElseFail(Response.notFound)

      //  } yield dbr.getUser(username, UserPassword.apply(password)) match {
      //    case Some(_) => Response.text(jwtEncode(username, ""))
      //    case None => Response.unauthorized("Invalid username or password.")
      //  }

      userOpt
        <- dbr
        .getUser(username, UserPassword.apply(password))
        .pipe(ZIO.succeed)

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
