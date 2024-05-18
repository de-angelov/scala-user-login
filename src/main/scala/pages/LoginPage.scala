package userlogin.pages


import zio._
import zio.http._
import zio.http.template._

private val headerHtml = head(title("Login Page !"))

private val contentHtml
  = div
  (
    h1("Login Page !")
  )

private val fullHtml =
       html
        ( headerHtml
        , body(contentHtml)
        )

val renderLoginPage = Handler.html(fullHtml)
