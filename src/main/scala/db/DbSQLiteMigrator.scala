package userlogin.db

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateErrorResult
import zio.{Task, ZIO, ZLayer}

import javax.sql.DataSource

class DbSQLiteMigrator
  (ds: DataSource) extends DbMigratorT {

  def migrate
    : Task[Unit]
    = ZIO
    .attempt(
      Flyway
        .configure()
        .locations("filesystem:./sql/postgres")
        .dataSource(ds)
        .load()
        .migrate()
    )
    .flatMap {
      case r: MigrateErrorResult => ZIO.fail(DbMigrationFailed(r.error.message, r.error.stackTrace))
      case _ => ZIO.succeed(())
    }
    .onError(cause => ZIO.logErrorCause("Database migration has failed", cause))
}


object DbSQLiteMigrator{
  def live
  : ZLayer[DataSource, Nothing, DbSQLiteMigrator]
  = ZLayer.fromFunction(DbSQLiteMigrator(_))
}

