package userlogin.types

case class AppConfig
  ( appPort: Int
  , appDBPool: Any
  )
// todo db pool

opaque type UserPassword = String

object UserPassword {
  def apply (value: String): UserPassword = value
}

opaque type UserInt = Long
object UserInt {
  def apply (value: Long): UserInt = value
}


opaque type HashedPassword = String
object HashedPassword {
  def apply (value: String): HashedPassword = value
}


case class UserLoginData
  ( username: String
  , password: UserPassword
  )

case class User
  ( username: String
  , id: Int
  , password: HashedPassword
  )
