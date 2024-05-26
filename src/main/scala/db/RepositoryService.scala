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
  private def buildQuery
    (crq: UserQuery )
    = crq

  def saveNewUser
    (username: String, password: UserPassword) : Task[Option[User]]
    = ???

  def getUser
    (username: String, password: UserPassword) : Option[User]
    =  ???

  def getAllUsers: Task[List[User]] = run(query[User])
}

object RepositoryService {
  val live = ZLayer.derive[RepositoryService]
}
