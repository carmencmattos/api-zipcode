package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import router.ZipCodeRouter
import service.ZipCodeService
import org.slf4j.LoggerFactory
object ZipCodeServer {
  private val logger = LoggerFactory.getLogger(getClass)
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("ZipCodeServer")
    implicit val executionContext = system.dispatcher

    val zipCodeService = new ZipCodeService()
    val routes = ZipCodeRouter.route(zipCodeService)

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)
    logger.info(s"Server online at http://localhost:8080/")

    sys.addShutdownHook{
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
      logger.info("Server offline")
    }
  }
}
