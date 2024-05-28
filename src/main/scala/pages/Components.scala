package userlogin.pages.components

import zio.http.template._


def item
  (href: String, text: String, isActive: Boolean): Dom
  = {
    val className = if (isActive) "active" else ""

    li
    (a
      (hrefAttr := href
      , classAttr := className
      , text
      )
    )
  }

def navbar
  (active: String): Dom
  = {
  val items
    = List
    (("home", "/")
    ,("login", "/login")
    ,("secret", "/secret")
    )

  val getElements
    : List[Dom]
    = items
    .map {
      case (text, href) => item(href, text, text == active)
    }

  nav(getElements)
  }
