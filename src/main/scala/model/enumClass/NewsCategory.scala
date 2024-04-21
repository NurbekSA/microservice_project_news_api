package model.enumClass

import spray.json.{JsString, JsValue, RootJsonFormat}
import slick.jdbc.MySQLProfile.api._
sealed trait NewsCategory
object NewsCategory {
  case object base extends NewsCategory
  case object block extends NewsCategory
  case object notifying extends NewsCategory

  implicit object NewsCategoryFormat extends RootJsonFormat[NewsCategory] {
    def write(obj: NewsCategory): JsString = JsString(obj.toString)

    def read(json: JsValue): NewsCategory = json match {
      case JsString("base") => base
      case JsString("block") => block
      case JsString("notifying") => notifying
      case _ => throw new IllegalArgumentException("Unknown UserStatus")
    }
  }

  implicit val roleColumnType: BaseColumnType[NewsCategory] =
    MappedColumnType.base[NewsCategory, String](
      r => r.toString,
      s => s match {
        case "base" => base
        case "block" => block
        case "notifying" => notifying
        case _ => throw new IllegalArgumentException(s"Unknown status: $s")
      }
    )
}
