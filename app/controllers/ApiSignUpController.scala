package controllers

import java.util.UUID

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers._
import formatters.json.Token
import forms.SignUpForm
import javax.inject.Inject
import models.services.{AuthTokenService, UserService}
import models.{SignUp, User}
import org.webjars.play.WebJarsUtil
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.{JsError, Json}
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import utils.auth.JwtEnv
import utils.responses.rest.Bad

import scala.concurrent.{ExecutionContext, Future}

/**
 * The `Sign Up` controller.
 *
 * @param components             The Play controller components.
 * @param silhouette             The Silhouette stack.
 * @param userService            The user service implementation.
 * @param authInfoRepository     The auth info repository implementation.
 * @param authTokenService       The auth token service implementation.
 * @param avatarService          The avatar service implementation.
 * @param passwordHasherRegistry The password hasher registry.
 * @param mailerClient           The mailer client.
 * @param webJarsUtil            The webjar util.
 * @param assets                 The Play assets finder.
 * @param ex                     The execution context.
 */
class ApiSignUpController @Inject()(
                                     components: ControllerComponents,
                                     silhouette: Silhouette[JwtEnv],
                                     userService: UserService,
                                     authInfoRepository: AuthInfoRepository,
                                     authTokenService: AuthTokenService,
                                     avatarService: AvatarService,
                                     passwordHasherRegistry: PasswordHasherRegistry,
                                     mailerClient: MailerClient
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def apiSignUp = Action.async(parse.json) { implicit request =>
    request.body.validate[SignUp].map { signUp =>
      val loginInfo = LoginInfo(CredentialsProvider.ID, signUp.identifier)
      userService.retrieve(loginInfo).flatMap {
        case None => /* user not already exists */
          val user = User(
            userID = UUID.randomUUID(),
            loginInfo = loginInfo,
            firstName = signUp.firstName,
            lastName = signUp.lastName,
            fullName = None,
            email = None,
            avatarURL = None,
            activated = false
          )

          val authInfo = passwordHasherRegistry.current.hash(signUp.password)
          for {
            userToSave <- userService.save(user)
            authInfo <- authInfoRepository.add(loginInfo, authInfo)
            authenticator <- silhouette.env.authenticatorService.create(loginInfo)
            token <- silhouette.env.authenticatorService.init(authenticator)
            result <- silhouette.env.authenticatorService.embed(
              token,
              Ok(Json.toJson(Token(token = token, expiresOn = authenticator.expirationDateTime)))
            )
          } yield {
            val url = routes.ApplicationController.index().absoluteURL()
            mailerClient.send(Email(
              subject = Messages("email.sign.up.subject"),
              from = Messages("email.from"),
              to = Seq(user.loginInfo.providerKey),
              bodyText = Some(views.txt.emails.signUp(user, url).body),
              bodyHtml = Some(views.html.emails.signUp(user, url).body)
            ))
            silhouette.env.eventBus.publish(SignUpEvent(user, request))
            silhouette.env.eventBus.publish(LoginEvent(user, request))
            result
          }
        case Some(_) => /* user already exists! */
          Future(Conflict(Json.toJson(Bad(message = "user already exists"))))
      }
    }.recoverTotal {
      case error =>
        Future.successful(BadRequest(Json.toJson(Bad(message = JsError.toJson(error)))))
    }
  }

}
