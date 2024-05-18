package userlogin.pages

import zio._
import zio.http._
import zio.http.template._

private val headerHtml = head(title("Home Page"))

private val contentHtml
  = div
  (
    h1("JUST Home Page")
  )

private val fullHtml =
       html
        ( headerHtml
        , body(contentHtml)
        )

val renderHomePage = Handler.html(fullHtml)
