package userlogin.types

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class AppConfig
  ( dbConn: String
  , dbPool: Int
  , jwtString: String
  )

opaque type UserPassword = String
object UserPassword {
  def apply (value: String): UserPassword = value
  given userPasswordEncoder: JsonEncoder[UserPassword] = DeriveJsonEncoder.gen
  given userPasswordDecoder: JsonDecoder[UserPassword] = DeriveJsonDecoder.gen
}

opaque type UserInt = Long
object UserInt {
  def apply (value: Long): UserInt = value
}

opaque type HashedPassword = String
object HashedPassword {
  def apply (value: String): HashedPassword = value
  def hash(value: UserPassword): HashedPassword = value
  // given hashedPasswordEncoder: JsonEncoder[HashedPassword] = DeriveJsonEncoder.gen
  // given hashedPasswordDecoder: JsonDecoder[HashedPassword] = DeriveJsonDecoder.gen
}


case class UserLoginData
  ( username: String
  , password: UserPassword
  )

object UserLoginData {
  given userLoginDataEncoder: JsonEncoder[UserLoginData] = DeriveJsonEncoder.gen
  given userLoginDataDecoder: JsonDecoder[UserLoginData] = DeriveJsonDecoder.gen
}

case class User
  ( username: String
  , id: Int
  , password: HashedPassword
  )

object User {
  given userEncoder: JsonEncoder[User] = DeriveJsonEncoder.gen
  given userDecoder: JsonDecoder[User] = DeriveJsonDecoder.gen
}
