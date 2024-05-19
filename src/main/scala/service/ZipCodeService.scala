package service

import akka.actor.ActorSystem
import handler.ZipCodeHandler
import handler.ZipCodeHandler.ZipCodeResponse

import scala.concurrent.{ExecutionContext, Future}

class ZipCodeService (implicit system: ActorSystem, ec: ExecutionContext){
  def getZipCode(zipcode: String): Future[Either[String, ZipCodeResponse]] = {
    ZipCodeHandler.fetchZipCode(zipcode).map { zipCodeResponse =>
      Right(zipCodeResponse)
    }.recover {
      case e: Throwable =>
        Left(s"Zip code not found: ${e.getMessage}")
    }
  }
}
