package userlogin.db

import userlogin.types._

import zio.*
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import java.time.Instant

import io.getquill.*
import io.getquill.jdbczio.*
import io.getquill.autoQuote

case class RepositoryService private(quill: Quill.Sqlite[SnakeCase]){
  import quill.*

  private inline def queryUser = quote(querySchema[User](entity = "users"))


  type UserQuery = Quoted[Query[User]]
  private def buildQuery(crq: UserQuery) = crq

  def saveNewUser
    (username: String, password: UserPassword): Task[Option[Int]]
    = run {
      queryUser
      .insert
        (_.username -> lift(username)
        , _.password -> lift(password)
        )
      .returningGenerated(_.id)
    }
    .option

  def getUser
    (username: String, password: UserPassword): Task[Option[User]]
    = run{
      queryUser
      .filter(x => x.username == username && x.password == HashedPassword.hash(password) )
    }
    .map{
        case x :: xs  => Some(x: User)
        case _ => None
      }

  def getAllUsers: Task[List[User]] = run(queryUser)
}

object RepositoryService {
  val live = ZLayer.derive[RepositoryService]
}
