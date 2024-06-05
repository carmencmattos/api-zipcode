package router

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import support.JsonFormats._
import handler.ZipCodeHandler
import service.ZipCodeService
import model.UserRequest
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.ExecutionContext
object ZipCodeRouter {
  def route(zipCodeService: ZipCodeService)(implicit system: ActorSystem, ec: ExecutionContext): Route = {
    path("zipcode")
        post {
          entity(as[UserRequest]) { userRequest =>
            onComplete(zipCodeService.getUserResponse(userRequest)) {
              case scala.util.Success(userResponse) =>
                complete(userResponse)
              case scala.util.Failure(ex) =>
                complete(500, s"""{"error": "Error processing the request: ${ex.getMessage}"}""")
            }
          }
        }
    }
  }
