package utils.auth

import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import play.api.Logger
import play.api.http.ContentTypes
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.Future


trait BaseHandler extends SecuredErrorHandler with I18nSupport with RequestExtractors with Rendering {

  val messagesApi: MessagesApi

  protected def produceResponse[S <: Status](status: S, msg: String, authenticated: Boolean)(implicit request: RequestHeader): Future[Result] =
    Future.successful(render {
      case Accepts.Json() => status(toJsonError(msg))
      case _ =>  if(authenticated) {
        Redirect(controllers.routes.SignInController.view()).flashing("error" -> Messages("access.denied"))
      } else {
        Redirect(controllers.routes.SignInController.view())
      }
    })

  protected def toJsonError(message: String) =
    Json.obj("message" -> message)

}
