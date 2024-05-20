package userlogin.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import zio.{Task, ZIO, ZLayer}
import javax.sql.DataSource
import scala.util.chaining.scalaUtilChainingOps
import org.flywaydb.core.api.output.MigrateErrorResult


class DbMigrator(ds: DataSource):
  def migrate
    (): Task[Unit]
    = Flyway
    .configure()
    .locations("filesystem:./sql")
    .dataSource(ds)
    .load()
    .migrate()
    .pipe(ZIO.attempt)
    .flatMap{
      case r: MigrateErrorResult
        => ZIO.fail(DbMigrationFailed(r.error.message, r.error.stackTrace))
      case _
        => ZIO.succeed(())
    }
    .onError(cause => ZIO.logErrorCause("Database migration has failed", cause))

case class DbMigrationFailed
  (msg: String, stackTrace: String)
  extends RuntimeException(s"$msg\n$stackTrace")

object DbMigrator:
  def live
    : ZLayer[DataSource, Nothing, DbMigrator]
    = ZLayer.fromFunction(DbMigrator(_))
