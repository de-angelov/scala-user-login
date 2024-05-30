package userlogin

import userlogin.api.{ApiEndpoints}
import userlogin.pages.{PagesEndpoints}
import userlogin.endpoints.{Endpoints}
import userlogin.db.{RepositoryService, DbMigratorT, DbSQLiteService, DbSQLiteMigrator, DbService, DbMigrator}
import userlogin.types.{AppConfig}

import zio.*
import zio.http.*
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import io.getquill.SqliteJdbcContext


val sqlite = "jdbc:sqlite:realworld-prod.sqlite"
val jwt = "#>!IEd!G-L70@OTr$t8E[4.#[A;zo2@{"

private def getEnvVar
  [T](name: String, default: T)(implicit ev: String => T): T
  = sys.env.get(name).map(ev).getOrElse(default)

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = SLF4J.slf4j(LogFormat.colored)

  override def run
    : ZIO[Any & (ZIOAppArgs & Scope), Any, Any]
    =
      val port = getEnvVar("HTTP_PORT", 8080)(_.toInt)
      val dbConn = getEnvVar("POSTGRES_CONNECT_STRING", sqlite)(x => x)
      val dbPool = getEnvVar("POSTGRES_POOL_SIZE", 2)(_.toInt)
      val jwtString = getEnvVar("JWK_STRING", jwt)(x => x)

      val appConfig = AppConfig( dbPool = dbPool, jwtString=jwtString, dbConn = dbConn)
      val app = ZIO.service[Endpoints].map{_.endpoints.toHttpApp }
      val program
        = for {
          _ <- ZIO.service[DbMigratorT].map(_.migrate)
          liftedApp <- app
          _ <- ZIO.logInfo("Starting app....")
          _ <- Server.serve(liftedApp @@ Middleware.debug)
          _ <- ZIO.logInfo("!!!!!!! DB POOL !!!!!!!!!"+dbPool)
          _ <- ZIO.logInfo("Server running")
          _ <- ZIO.never
        } yield ()

      program
      .provide(
        ZLayer.succeed(appConfig),
        Endpoints.live,
        PagesEndpoints.live,
        ApiEndpoints.live,
        RepositoryService.live,
        DbSQLiteService.dataSourceLive,
        DbSQLiteService.quillLive,
        DbSQLiteMigrator.live,
        Server.defaultWithPort(port),
      )
      .exitCode
}
