package userlogin.db.postgress

import userlogin.types.{AppConfig}

import io.getquill._
import io.getquill.jdbczio.Quill
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import javax.sql.DataSource
import zio.{ZIO, ZLayer}
import scala.util.chaining.scalaUtilChainingOps

object DbService {
  private def create
    (config: AppConfig): HikariDataSource
    = {
      ZIO.log("Setting up DB..." + config.dbConn)
      val poolConfig = new HikariConfig()
      poolConfig.setJdbcUrl(config.dbConn)
      new HikariDataSource(poolConfig)
    }

  val dataSourceLive
    : ZLayer[AppConfig, Nothing, DataSource]
    = ZLayer.scoped {
      ZIO.fromAutoCloseable {
        for {
          dataSource <- (ZIO.service[AppConfig]).map(create)
        } yield dataSource
      }
    }

  val quillLive
    : ZLayer [DataSource, Nothing, Quill.Postgres[SnakeCase]]
    = Quill.Postgres.fromNamingStrategy(SnakeCase)
}

