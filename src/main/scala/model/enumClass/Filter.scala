package model.enumClass

import spray.json.{JsString, JsValue, RootJsonFormat}
import slick.jdbc.MySQLProfile.api._
sealed trait Filter
object Filter {
  case object holiday extends Filter
  case object update extends Filter
  case object event extends Filter
  case object development extends Filter

  implicit object FilterFormat extends RootJsonFormat[Filter] {
    def write(obj: Filter): JsString = JsString(obj.toString)

    def read(json: JsValue): Filter = json match {
      case JsString("holiday") => holiday
      case JsString("update") => update
      case JsString("event") => event
      case JsString("development") => development
      case _ => throw new IllegalArgumentException("Unknown UserStatus")
    }
  }

  implicit val roleColumnType: BaseColumnType[Filter] =
    MappedColumnType.base[Filter, String](
      r => r.toString,
      s => s match {
        case "holiday" => holiday
        case "update" => update
        case "event" => event
        case "development" => development
      }
    )
}
