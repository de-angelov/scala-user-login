package userlogin.db

import zio.{Task}

trait DbMigratorT {
  def migrate: Task[Unit]
}

case class DbMigrationFailed
  (msg: String, stackTrace: String)
  extends RuntimeException(s"$msg\n$stackTrace")
