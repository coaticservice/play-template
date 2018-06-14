package jobs.circuiBreaker

import akka.actor.ActorSystem
import akka.pattern.CircuitBreaker
import javax.inject.Inject
import play.api.libs.ws.{WSClient, WSResponse}
import utils.Logger

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class JobStarter @Inject()(ws: WSClient)(implicit actorSystem: ActorSystem, implicit val executionContext: ExecutionContext) extends Logger {

  val breaker =
    new CircuitBreaker(
      actorSystem.scheduler,
      maxFailures = 2,
      callTimeout = 3.seconds,
      resetTimeout = 25.seconds)
      .onOpen(println("circuit breaker opened"))
      .onClose(println("circuit breaker closed"))
      .onHalfOpen(println("circuit breaker half-open"))

  Future {
    while (true) {
      val http404 = "https://httpstat.us/400"
      val randomNumnber = "https://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new"
      val fResponse = ws.url(url = http404).get()
      breaker.withCircuitBreaker(fResponse, isHttpOkStatus).map(resp => resp.status match {
        case 200 => logger.info("success: " + resp.body)
        case _  => logger.info("error: " + resp.status)
      })

      Thread.sleep(1000)
    }
  }

  val isHttpOkStatus: Try[WSResponse] ⇒ Boolean = {
    case Success(n) ⇒ n.status != 200
    case Failure(_) ⇒ true
  }

}
