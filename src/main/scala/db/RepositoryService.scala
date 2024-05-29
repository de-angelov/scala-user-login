package userlogin.db

import userlogin.types._

import zio.*

import io.getquill.*
import io.getquill.jdbczio.*

case class UserRow
  ( username: String
  , id: Int
  , password: String
  )

val dbrowToUser
  : UserRow => User
  = u => User(
    id =u.id,
    password = HashedPassword(u.password),
    username = u.username
  )

case class RepositoryService private(quill: Quill.Postgres[SnakeCase]){
  import quill.*

  private inline def queryUser
    = quote(querySchema[UserRow](entity = "users"))

  // def saveNewUser(username: String, password: UserPassword): UIO[Option[User]] = ???

  // def getUser(username: String, password: HashedPassword): UIO[Option[User]] = ???

  def saveNewUser
    (username: String, password: UserPassword): UIO[Option[Int]]
    =
      val pass = HashedPassword.toString(HashedPassword.hash(password))

      run {
      queryUser
      .insert
        (_.username -> lift(username)
        , _.password -> lift(pass)
        )
      .returningGenerated(_.id)
    }
    .option

  def getUser
    (username: String, password: HashedPassword)
    : UIO[Option[User]]
    = run{
        queryUser
        .filter(x => x.username == lift(username) && x.password == lift(password.toString()))
      }
      .map(x => x.map(dbrowToUser))
      .option
      .map{
        case Some(x :: xs) => Some(x)
        case _ => None
      }

  def getAllUsers
    : UIO[List[User]]
    =run(queryUser)
    .map(x => x.map(dbrowToUser))
    .either
    .map {
      case Right(value) => value
      case Left(_) => List.empty[User]
    }
}

object RepositoryService {
  val live = ZLayer.derive[RepositoryService]
}
