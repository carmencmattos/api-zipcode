package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import router.ZipCodeRouter
import service.ZipCodeService

import scala.concurrent.ExecutionContext

object ZipCodeServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("ZipCodeServer")
    implicit val executionContext: ExecutionContext = system.dispatcher

    val zipCodeService = new ZipCodeService()(system, executionContext)
    val bindingFuture = Http().newServerAt("localhost", 8080).bindFlow(ZipCodeRouter.route(zipCodeService)(system))

    println(s"Server online at http://localhost:8080/")

  }
}
