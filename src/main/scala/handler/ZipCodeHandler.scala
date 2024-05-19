package handler

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import spray.json.DefaultJsonProtocol._
import spray.json._
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.duration._

object ZipCodeHandler {
  private val logger = LoggerFactory.getLogger(getClass)

  case class ZipCodeRequest(zipcode: String)
  implicit val zipCodeRequestFormat: RootJsonFormat[ZipCodeRequest] = jsonFormat1(ZipCodeRequest)

  case class ZipCodeResponse(
    cep: String,
    logradouro: String,
    complemento: String,
    bairro: String,
    localidade: String,
    uf: String,
    ibge: String,
    gia: String,
    ddd: String,
    siafi: String
                            )

  implicit val zipCodeResponseFormat: RootJsonFormat[ZipCodeResponse] = jsonFormat10(ZipCodeResponse.apply)

  def fetchZipCode(zipcode: String)(implicit system: akka.actor.ActorSystem, ec: scala.concurrent.ExecutionContext): Future[ZipCodeResponse] = {
    logger.debug(s"Fetching zip code: $zipcode")
    Http()
      .singleRequest(HttpRequest(uri = s"https://viacep.com.br/ws/$zipcode/json/"))
      .flatMap { response =>
        response.entity.toStrict(10.seconds).map { strictEntity =>
          logger.debug(s"Zip code response: ${strictEntity.data.utf8String}")
          strictEntity.data.utf8String.parseJson.convertTo[ZipCodeResponse]
        }
      }
  }
  def formatZipCode(zipCodeResponse: ZipCodeResponse): String =
    s"${zipCodeResponse.cep}, ${zipCodeResponse.logradouro}, ${zipCodeResponse.complemento}, ${zipCodeResponse.bairro}, ${zipCodeResponse.localidade}, ${zipCodeResponse.uf}, ${zipCodeResponse.ibge}, ${zipCodeResponse.gia}, ${zipCodeResponse.ddd}, ${zipCodeResponse.siafi}"
}
