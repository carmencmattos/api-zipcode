package handler

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import spray.json._
import support.JsonFormats._
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
object ZipCodeHandler {
  private val logger = LoggerFactory.getLogger(getClass)
  case class UserRequest(nome: String, contato: String, cep: String)
  case class ZipCodeResponse(cep: String, logradouro: String, complemento: String, bairro: String, localidade: String, uf: String, ibge: String, gia: String, ddd: String, siafi: String)
  case class UserResponse(nome: String, contato: String, cep: String, logradouro: String, complemento: String, bairro: String, localidade: String, uf: String, ibge: String, gia: String, ddd: String, siafi: String)

  def fetchZipCode(zipcode: String)(implicit system: ActorSystem, ec: ExecutionContext): Future[ZipCodeResponse] = {
    logger.debug(s"Fetching zip code $zipcode")
    Http().singleRequest(HttpRequest(uri = s"https://viacep.com.br/ws/$zipcode/json/"))
      .flatMap { response =>
        response.entity.toStrict(10.seconds).map { strictEntity =>
          val responseData = strictEntity.data.utf8String
          logger.debug(s"Zip code response: ${responseData}")
          responseData.parseJson.convertTo[ZipCodeResponse]
        }
      }
    }

  def createUserResponse(userRequest: UserRequest, zipCodeResponse: ZipCodeResponse): UserResponse = {
    UserResponse(
      nome = userRequest.nome,
      contato = userRequest.contato,
      cep = zipCodeResponse.cep,
      logradouro = zipCodeResponse.logradouro,
      complemento = zipCodeResponse.complemento,
      bairro = zipCodeResponse.bairro,
      localidade = zipCodeResponse.localidade,
      uf = zipCodeResponse.uf,
      ibge = zipCodeResponse.ibge,
      gia = zipCodeResponse.gia,
      ddd = zipCodeResponse.ddd,
      siafi = zipCodeResponse.siafi
    )
  }
}
