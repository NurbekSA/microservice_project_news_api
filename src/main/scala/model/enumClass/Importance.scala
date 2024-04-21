
package model.enumClass

import spray.json.{JsString, JsValue, RootJsonFormat}
import slick.jdbc.MySQLProfile.api._
sealed trait Importance
object Importance {
  case object high extends Importance
  case object medium extends Importance
  case object low extends Importance

  implicit object ImportanceFormat extends RootJsonFormat[Importance] {
    def write(obj: Importance): JsString = JsString(obj.toString)

    def read(json: JsValue): Importance = json match {
      case JsString("high") => high
      case JsString("medium") => medium
      case JsString("low") => low
      case _ => throw new IllegalArgumentException("Unknown UserStatus")
    }
  }

  implicit val roleColumnType: BaseColumnType[Importance] =
    MappedColumnType.base[Importance, String](
      r => r.toString,
      s => s match {
        case "high" => high
        case "medium" => medium
        case "low" => low
        case _ => throw new IllegalArgumentException(s"Unknown status: $s")
      }
    )
}
