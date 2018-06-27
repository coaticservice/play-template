package controllers

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.util.Clock
import com.mohiva.play.silhouette.impl.providers._
import javax.inject.Inject
import models.services.UserService
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.JwtEnv

import scala.concurrent.ExecutionContext

/**
 * The `Sign In` controller.
 *
 * @param components             The Play controller components.
 * @param silhouette             The Silhouette stack.
 * @param userService            The user service implementation.
 * @param credentialsProvider    The credentials provider.
 * @param socialProviderRegistry The social provider registry.
 * @param configuration          The Play configuration.
 * @param clock                  The clock instance.
 */
class ApiSignInController @Inject()(
                                     components: ControllerComponents,
                                     silhouette: Silhouette[JwtEnv],
                                     userService: UserService,
                                     credentialsProvider: CredentialsProvider,
                                     socialProviderRegistry: SocialProviderRegistry,
                                     configuration: Configuration,
                                     clock: Clock
)(
  implicit ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {


}
