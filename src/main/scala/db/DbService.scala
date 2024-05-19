package userlogin.db

import userlogin.types.{AppConfig}

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.*
import io.getquill.jbczio.*
import javax.sql.DataSource
import quill._
import zio.{ZIO, ZLayer}

object DbService {
  private def create
    (config: AppConfig): HikariDataSource
    = {
      val poolConfig = new HikariConfig()
      poolConfig.setJbcUrl(config.dbPool)
      poolConfig.setJbcUrl(config.dbConn)

      new HikariDataSource(poolConfig)
    }

  val dataSourceLive
    : ZLayer[DbConfig, Nothing, DataSource]
    = ZLayer.scoped {
      ZIO.fromAutoCloseable {
        for {
          dbConfig <- ZIO.service[AppConfig]
          dataSource <- AppConfig.pipe(create).pipe(ZIO.succeed)
        } yield dataSource
      }
    }

  val quillLive
    : ZLayer [DataSource, Nothing, Quill.Postgres[SnakeCase]]
    = Quill.Postgres.fromNamingStrategy(SnakeCase)
}

