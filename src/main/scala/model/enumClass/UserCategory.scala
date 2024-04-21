package model.enumClass

import slick.jdbc.MySQLProfile.api._
import slick.lifted.MappedTo
import spray.json.{JsString, JsValue, RootJsonFormat}
//
sealed trait UserCategory
object UserCategory {
  case object student extends UserCategory
  case object teacher extends UserCategory
  case object admin extends UserCategory

  implicit object PetitionStatusFormat extends RootJsonFormat[UserCategory] {
    def write(obj: UserCategory): JsString = JsString(obj.toString)

    def read(json: JsValue): UserCategory = json match {
      case JsString("student") => student
      case JsString("teacher") => teacher
      case JsString("admin") => admin
      case _ => throw new IllegalArgumentException("Unknown UserStatus")
    }
  }

  implicit val roleColumnType: BaseColumnType[UserCategory] =
    MappedColumnType.base[UserCategory, String](
      r => r.toString,
      s => s match {
        case "student" => student
        case "teacher" => teacher
        case "admin" => admin
        case _ => throw new IllegalArgumentException(s"Unknown status: $s")
      }
    )
}
