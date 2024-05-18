package userlogin.pages


import zio._
import zio.http._
import zio.http.template._

private val headerHtml = head(title("Secret Page"))

private val contentHtml
  = div
  (
    h1("MOST Secret Page")
  )

private val fullHtml =
       html
        ( headerHtml
        , body(contentHtml)
        )

val renderSecretPage = Handler.html(fullHtml)
