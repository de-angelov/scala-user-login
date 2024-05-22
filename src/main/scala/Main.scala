package userlogin

import userlogin.api.{ApiEndpoints}
import userlogin.pages.{PagesEndpoints}
import userlogin.endpoints.{Endpoints}
import userlogin.db.{RepositoryService, DbService, DbMigrator}
import userlogin.types.{AppConfig}

import zio.*
import zio.http.*
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import userlogin.db.DbMigrator

private def getEnvVar
  [T](name: String, default: T)(implicit ev: String => T): T
  = sys.env.get("HTTP_PORT").map(ev).getOrElse(default)

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = SLF4J.slf4j(LogFormat.colored)

  override def run
    : ZIO[Any & (ZIOAppArgs & Scope), Any, Any]
    =
      val port = getEnvVar("HTTP_PORT", 8080)(_.toInt)
      val dbConn = getEnvVar("POSTGRES_CONNECT_STRING", "")(x => x)
      val dbPool = getEnvVar("POSTGRES_POOL_SIZE", 4)(_.toInt)
      val jwtString = getEnvVar("JWK_STRING", "")(x => x)

      val appConfig = AppConfig( dbPool = dbPool, jwtString=jwtString, dbConn = dbConn)
      val app = ZIO.service[Endpoints].map{_.endpoints.toHttpApp }

      val program
        = for {
          _ <- ZIO.service[DbMigrator].map(_.migrate())

          liftedApp <- app
          _ <- ZIO.logInfo("Starting app....")
          _ <- Server.serve(liftedApp @@ Middleware.debug)
          _ <- ZIO.logInfo("Server running")
        } yield ()

      program
      .provide(
        Endpoints.live,
        PagesEndpoints.live,
        ApiEndpoints.live,
        RepositoryService.live,
        DbService.quillLive,
        DbService.dataSourceLive,
        DbMigrator.live,
        ZLayer.succeed(appConfig),
        Server.defaultWithPort(port),
      )
      .exitCode
}
