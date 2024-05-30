package userlogin.types

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}
import zio.json.JsonCodec


case class AppConfig
  ( dbConn: String
  , dbPool: Int
  , jwtString: String
  )

opaque type UserPassword = String
object UserPassword {
  def apply (value: String): UserPassword = value

  // Encoder
  given userPasswordEncoder: JsonEncoder[UserPassword] = JsonEncoder.string.contramap[UserPassword](identity)
  // Decoder
  given userPasswordDecoder: JsonDecoder[UserPassword] = JsonDecoder.string.map(UserPassword(_))

}

opaque type UserInt = Long
object UserInt {
  def apply (value: Long): UserInt = value
}

opaque type HashedPassword = String
object HashedPassword {
  def apply (value: String): HashedPassword = value
  def hash(value: UserPassword | String): HashedPassword = value
  def toString(value: HashedPassword): String = value
    // Encoder
  // given userPasswordEncoder: JsonEncoder[HashedPassword] = JsonEncoder.string.contramap[HashedPassword](x => x)
  // // Decoder
  // given userPasswordDecoder: JsonDecoder[HashedPassword] = JsonDecoder.string.map(HashedPassword.apply)

  given (using ev: JsonCodec[String]): JsonCodec[HashedPassword] = ev
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
