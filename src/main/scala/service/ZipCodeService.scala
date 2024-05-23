package service

import akka.actor.ActorSystem
import handler.ZipCodeHandler
import handler.ZipCodeHandler.{UserRequest, UserResponse}

import scala.concurrent.{ExecutionContext, Future}
class ZipCodeService(implicit system: ActorSystem, ec: ExecutionContext) {
  def getUserResponse(userRequest: UserRequest): Future[UserResponse] = {
    ZipCodeHandler.fetchZipCode(userRequest.cep).map { zipCodeResponse =>
      ZipCodeHandler.createUserResponse(userRequest, zipCodeResponse)
    }
  }
}
