package support

import spray.json._
import spray.json.DefaultJsonProtocol._
import handler.ZipCodeHandler.{UserRequest, ZipCodeResponse, UserResponse}

object JsonFormats {
  implicit val userRequestFormat: RootJsonFormat[UserRequest] = jsonFormat3(UserRequest)
  implicit val zipCodeResponseFormat: RootJsonFormat[ZipCodeResponse] = jsonFormat10(ZipCodeResponse)
  implicit val userResponseFormat: RootJsonFormat[UserResponse] = jsonFormat12(UserResponse)
}