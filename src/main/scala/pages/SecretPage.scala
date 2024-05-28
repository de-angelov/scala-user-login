package userlogin.pages.secret


import zio._
import zio.http._
import zio.http.template._

import userlogin.pages.components.{navbar}
import userlogin.types.{User}

val js
  = """
    function logoff() {
    fetch("/api/login",
      {
        method: 'DELETE',
        headers: new Headers({'content-type': 'application/json'}),
      })
    .then(_ => window.location.href = '/login')
    .catch(err => {
      console.log('error', err);
    });
    }
  """

private val headerHtml = head(title("Secret Page"))

private val contentHtml
  : (String) => Dom
  = (username)
  // => ("test")
  => div
  (navbar("secret")
  ,h1("MOST Secret Page")
  ,h2("Hello" ++ username)
  ,script
    (typeAttr := "text/javascript"
    ,js)
  )

private val fullHtml
  : (String) => Dom
  = (username)
  => {

  html
    ( headerHtml
    , body(
      contentHtml(username)
      )
    )
}




val renderSecretPage = (username: String) => Handler.html(fullHtml(username))
