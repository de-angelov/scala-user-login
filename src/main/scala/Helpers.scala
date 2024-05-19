package userlogin.helpers
import zio._
import zio.http._
import java.time.Clock
import scala.util.Try
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import scala.util.chaining.scalaUtilChainingOps



given clock: Clock = Clock.systemUTC

def jwtEncode
  (username: String, key: String, duration: Int = 300 ): String
  = JwtClaim(subject = Some(username))
  .issuedNow
  .expiresIn(duration)
  .pipe(x => Jwt.encode(x, key, JwtAlgorithm.HS512))


def jwtDecode
  (token: String, key: String): Try[JwtClaim]
  =Jwt
  .decode
    ( token
    , key
    , Seq(JwtAlgorithm.HS512)
    )

def bearerAuthWithContext
  (secretKey: String): HandlerAspect[Any, String]
  =
      val handleToken
        = (request: Request)
        => (token: String)
        =>
        jwtDecode(token, secretKey)
          .pipe(ZIO.fromTry)
          .orElseFail(Response.badRequest("Invalid or expired token!"))
          .flatMap(
            _
            .subject
            .pipe(ZIO.fromOption)
            .orElseFail(Response.badRequest("Missing subject claim!"))
            .map(u => (request, u))
          )

      val handleNoToken
          = { val header = Header.WWWAuthenticate.Bearer(realm = "Access")
          Response
          .unauthorized
          .addHeader(header)
          .pipe(ZIO.fail)
          }

      val handleRequest
        = (request: Request) =>
        type Bearer = Header.Authorization.Bearer

        request
        .header(Header.Authorization) match
          case Some(Bearer(token)) => handleToken(request)(token.value.toString)
          case _ => handleNoToken

      Handler
      .fromFunctionZIO[Request](handleRequest)
      .pipe(HandlerAspect.interceptIncomingHandler)


