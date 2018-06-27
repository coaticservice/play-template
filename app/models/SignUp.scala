package models

import play.api.libs.json.{Json, OFormat}

case class SignUp(
  identifier: String,
  password: String,
  firstName: Option[String],
  lastName: Option[String])

object SignUp {

  implicit val jsonFormat: OFormat[SignUp] = Json.format[SignUp]

}