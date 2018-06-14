package controllers

import com.mohiva.play.silhouette.api.{LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import javax.inject.{Inject, Singleton}
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.auth.CookieEnv

import scala.concurrent.Future

@Singleton
class ApplicationController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[CookieEnv]) (
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
)extends AbstractController(components) with I18nSupport {

  /**
    * Handles the index action.
    *
    * @return The result to display.
    */
  def index = silhouette.SecuredAction.async { implicit request: SecuredRequest[CookieEnv, AnyContent] =>
    Future.successful(Ok(views.html.home(request.identity)))
  }


  /**
    * Handles the Sign Out action.
    *
    * @return The result to display.
    */
  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[CookieEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def docs = Action { implicit request =>
    Redirect(
      url = "/docs/index.html",
      queryString = Map("url" -> Seq(
        if (request.host.contains(":9000"))
          "http://" + request.host + "/swagger.json"
        else
          "https://" + request.host + "/swagger.json"
      ))
    )
  }
}