package com.example.json

import java.time.LocalDateTime
import spray.json._

final case class ParameterValue(utc: String, value: String)
final case class ParameterFile(starttime: String, name: String, values: List[ParameterValue])

final case class TelemetryParameter(doy:Int, name:String, utc: String, value: String)

object ParameterFileJsonProtocol extends DefaultJsonProtocol {
  implicit object ParameterFileJson extends RootJsonFormat[ParameterFile] {
    def write(c: ParameterFile) = JsObject(
      "values" -> JsArray(c.values.map {
        case ParameterValue(utc, value) => JsObject(
          "time" -> JsString(utc),
          "value" -> JsString(value)
        )
      }.toVector)
    )

    def read(value: JsValue): ParameterFile = value match {
      case _ => new ParameterFile("", "", List.empty)
    }
  }
}