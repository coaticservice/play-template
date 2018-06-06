package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi

import reactivemongo.api._
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection._
import reactivemongo.play.json.collection.JSONCollection

case class PersistentPasswordInfo(loginInfo: LoginInfo, authInfo: PasswordInfo)

/**
 * The DAO to store the password information.
 */
class PasswordInfoDAO @Inject() (val reactiveMongoApi: ReactiveMongoApi)(implicit ex: ExecutionContext) extends DelegableAuthInfoDAO[PasswordInfo] {

  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val persistentPasswordInfoFormat = Json.format[PersistentPasswordInfo]

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("password"))

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo) = {

    val passwordInfo: Future[Option[PersistentPasswordInfo]] = collection.flatMap(
      _.find(Json.obj("loginInfo" -> loginInfo))
        .one[PersistentPasswordInfo])

    passwordInfo.flatMap {
      case None =>
        Future.successful(Option.empty[PasswordInfo])
      case Some(persistentPasswordInfo) =>
        Future(Some(persistentPasswordInfo.authInfo))
    }

  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    collection.flatMap(_.insert(PersistentPasswordInfo(loginInfo, authInfo)))
    Future.successful(authInfo)
  }

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    Future.successful(authInfo)
  }

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = {
    Future.successful(())
  }
}