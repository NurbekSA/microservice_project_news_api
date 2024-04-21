package repository

import akka.http.scaladsl.marshallers.sprayjson._
import spray.json._
import model._
import model.newsComponents._
import model.newsComponents.blockNewsComponents._
import slick.jdbc.MySQLProfile.api._

import java.sql.Date
import java.time.LocalDate
import scala.collection.mutable.ListBuffer

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  import spray.json._

  implicit val sqlDateFormat: RootJsonFormat[Date] = new RootJsonFormat[Date] {
    def write(date: Date): JsString = JsString(date.toString)

    def read(json: JsValue): Date = json match {
      case JsString(s) => Date.valueOf(s)
      case _ => throw new IllegalArgumentException("Invalid Date format")
    }
  }
  implicit val dateColumnType: BaseColumnType[Date] =
    MappedColumnType.base[Date, Long](
      date => date.getTime,
      millis => new Date(millis)
    )

  implicit val NotifyingNewsFormat: RootJsonFormat[NotifyingNewsModel] = jsonFormat5(NotifyingNewsModel)
  implicit val NotifyingNewsListFormat: RootJsonFormat[List[NotifyingNewsModel]] = listFormat(NotifyingNewsFormat)

  implicit val CommentModelFormat: RootJsonFormat[CommentModel] = jsonFormat8(CommentModel)
  implicit val CommentModelListFormat:RootJsonFormat[List[CommentModel]]=listFormat(CommentModelFormat)


  implicit val NotifyingNewsAssemblyFormat: RootJsonFormat[NotifyingNewsAssemblyModel] = jsonFormat2(NotifyingNewsAssemblyModel)
  implicit val NotifyingNewsAssemblyListFormat:RootJsonFormat[List[NotifyingNewsAssemblyModel]]=listFormat(NotifyingNewsAssemblyFormat)

  implicit val NewsBlockFormat: RootJsonFormat[NewsBlockModel] = jsonFormat6(NewsBlockModel)
  implicit val NewsBlockListFormat: RootJsonFormat[List[NewsBlockModel]] = listFormat(NewsBlockFormat)

  implicit val NewsBlockPrimeFormat: RootJsonFormat[NewsBlockPrimeModel] = jsonFormat11(NewsBlockPrimeModel)
  implicit val NewsBlockPrimeListFormat: RootJsonFormat[List[NewsBlockPrimeModel]] = listFormat(NewsBlockPrimeFormat)

  implicit val NewsFormat: RootJsonFormat[NewsModel] = jsonFormat14(NewsModel)
  implicit val NewsListFormat: RootJsonFormat[List[NewsModel]] = listFormat(NewsFormat)

  implicit val blockNewsAssemblyFormat: RootJsonFormat[BlockNewsAssemblyModel] = jsonFormat3(BlockNewsAssemblyModel)
  implicit val blockNewsAssemblyListFormat: RootJsonFormat[List[BlockNewsAssemblyModel]] = listFormat(blockNewsAssemblyFormat)

  implicit val NewsAssemblyFormat: RootJsonFormat[NewsAssemblyModel] = jsonFormat2(NewsAssemblyModel)
  implicit val NewsAssemblyListFormat: RootJsonFormat[List[NewsAssemblyModel]] = listFormat(NewsAssemblyFormat)

}
