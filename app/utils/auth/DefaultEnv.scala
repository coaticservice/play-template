package utils.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, JWTAuthenticator}
import models.User

trait JwtEnv extends Env {

  type I = User
  type A = JWTAuthenticator
}

trait CookieEnv extends Env {

  type I = User
  type A = CookieAuthenticator
}