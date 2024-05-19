package router

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import handler.ZipCodeHandler._
import service.ZipCodeService
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object ZipCodeRouter extends SprayJsonSupport {

  implicit val zipCodeRequestFormat: RootJsonFormat[ZipCodeRequest] = jsonFormat1(ZipCodeRequest)
  implicit val zipCodeResponseFormat: RootJsonFormat[ZipCodeResponse] = jsonFormat10(ZipCodeResponse)

  def route(zipCodeService: ZipCodeService)(implicit system: ActorSystem): Route = {
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    get {
      path("zipcode" / Segment) { zipcode =>
          onSuccess(zipCodeService.getZipCode(zipcode)) {
            case Right (zipCodeResponse) => complete(StatusCodes.OK, zipCodeResponse)
            case Left(errorMessage) => complete(StatusCodes.NotFound, errorMessage)
          }
        }
      }
    }
  }
