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

opaque type HashedPassword = String

case class UserLoginData
  ( username: String
  , password: UserPassword
  )

case class User
  ( username: String
  , id: Int
  , password: HashedPassword
  )
