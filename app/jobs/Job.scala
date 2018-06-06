package jobs

import javax.inject.Inject
import akka.actor._
import scala.concurrent.duration._
import utils.Logger

import scala.concurrent.ExecutionContext

/**
 * A example job.
 */
class Job @Inject() (actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) extends Logger {

  actorSystem.scheduler.schedule(initialDelay = 10.seconds, interval = 1.minute) {
    // the block of code that will be executed
    logger.info("Executing something...")
  }
}