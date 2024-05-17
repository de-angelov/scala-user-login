package userlogin

import userlogin.api.{ApiEndpoints}
import userlogin.pages.{PagesEndpoints}
import userlogin.endpoints.{Endpoints}
import userlogin.db.{RepositoryService}

import zio.*
import zio.http.*
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = SLF4J.slf4j(LogFormat.colored)

  override def run
    : ZIO[Any & (ZIOAppArgs & Scope), Any, Any]
    = {

      val port = sys.env.get("HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)

      val app = ZIO.service[Endpoints].map{_.endpoints.toHttpApp }

      val program
        = for {
          endpoints <- app
          _ <- ZIO.logInfo("Starting..")
          _ <- Server.serve(endpoints @@ Middleware.debug @@ Middleware.flashScopeHandling )
          _ <- ZIO.logInfo("Server running")

        } yield ()

      program
      .provide(
        Endpoints.live,
        PagesEndpoints.live,
        ApiEndpoints.live,
        RepositoryService.live,
        Server.defaultWithPort(port)
      )
      .exitCode

      }
    }
