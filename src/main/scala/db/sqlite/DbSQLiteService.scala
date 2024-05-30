package userlogin.db.sqlite

import userlogin.types.{AppConfig}

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.*
import io.getquill.jdbczio.*
import zio.{ ZIO, ZLayer}

import javax.sql.DataSource

object DbSQLiteService {

  private def create(dbConfig: AppConfig): HikariDataSource
    =
    val poolConfig = new HikariConfig()
    poolConfig.setJdbcUrl(dbConfig.dbConn)
    new HikariDataSource(poolConfig)

  // Used for migration and executing queries.
  val dataSourceLive: ZLayer[AppConfig, Nothing, DataSource] =
    ZLayer.scoped {
      ZIO.fromAutoCloseable {
        for {
          dbConfig <- ZIO.service[AppConfig]
          dataSource <- ZIO.succeed(create(dbConfig))
        } yield dataSource
      }
    }

  // Quill framework object used for specifying sql queries.
  val quillLive: ZLayer[DataSource, Nothing, Quill.Sqlite[SnakeCase]] =
    Quill.Sqlite.fromNamingStrategy(SnakeCase)
}
