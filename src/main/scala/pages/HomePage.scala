package userlogin.pages.home

import zio._
import zio.http._
import zio.http.template._

import userlogin.pages.components.{navbar}

private val headerHtml = head(title("Home Page"))

private val contentHtml
  = div
  ( navbar("home")
  , h1("Home sweet home")
  , p("Lorem")
  )

private val fullHtml =

      html
        ( headerHtml
        , body(contentHtml)
        )

val renderHomePage = Handler.html(fullHtml)
