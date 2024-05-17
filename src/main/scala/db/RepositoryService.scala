package userlogin.db

import userlogin.types._
import zio.ZLayer
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}


case class RepositoryService private(){

  def saveNewUser
    (username: String, password: UserPassword) : Option[User]
    = ???

  def getUser
    (username: String, password: UserPassword) : Option[User]
    = ???

  def getAllUsers
    : List[User] = ???
}

object RepositoryService {
  val live = ZLayer.derive[RepositoryService]
}
