package userlogin.pages.login


import zio._
import zio.http._
import zio.http.template._

import userlogin.pages.components.{navbar}

val js =
  """
  |  var form = document.getElementById('form');
  |
  |
  |  function handleForm(event) { event.preventDefault(); }
  |  form.addEventListener('submit', handleForm);
  |
  |  var form = document.getElementById('form');
  |  var password = document.getElementById('password')?.value;
  |  var username = document.getElementById('username')?.value;
  |
  |
  |  function register() {
  |  fetch('/api/register',
  |  { method: 'POST',
  |    body: JSON.stringify({ username, password }),
  |    headers: new Headers({'content-type': 'application/json'}),
  |  })
  |    .then(login)
  |    .catch(err => {
  |      alert('error', err);
  |    });
  |  }
  |
  |  function login() {
  |  fetch('/api/login',
  |  {method: 'POST',
  |  body: JSON.stringify({ username, password }),
  |  headers: new Headers({'content-type': 'application/json'}),
  |  })
  |  .then(_ => window.location.href = '/secret')
  |  .catch(err => {
  |    alert('error', err);
  |  });
  |  }function register() {
  |  fetch('/api/register',
  |  { method: 'POST',
  |    body: JSON.stringify({ username, password }),
  |    headers: new Headers({'content-type': 'application/json'}),
  """


private val headerHtml = head(title("Login Page !"))

private val contentHtml
  = div
  (navbar("login")
  ,h1("Login Page !")
  ,input
    (idAttr := "username"
    ,typeAttr := "text"
    ,nameAttr := "username"
    )
  ,input
    (idAttr := "password"
    ,typeAttr := "text"
    ,nameAttr := "password"
    )
  ,input
    (onClickAttr := "register()"
    ,typeAttr := "submit"
    ,nameAttr := "submit"
    )
  ,script
    (typeAttr := "text/javascript"
    ,js)
  )

private val fullHtml
  = html
  ( headerHtml
  , body(contentHtml)
  )

val renderLoginPage = Handler.html(fullHtml)
