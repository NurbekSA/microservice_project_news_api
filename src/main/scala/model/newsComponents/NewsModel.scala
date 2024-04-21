package model.newsComponents

import model.enumClass.{Filter, Importance, UserCategory}
import slick.jdbc.MySQLProfile.api._

import java.sql.Date

case class NewsModel(id: Option[Int],
                     authorId: Int,
                     canComment: Boolean,
                     userCategory: UserCategory,
                     filter: Filter,
                     importance: Importance,
                     date: Date,
                     lifetime: Date,
                     content: String,
                     titel:String,
                     sampleNumber: String, // номер шаблона по умолчанию
                     img: String, // img это имя изображения внутри папки images
                     like: Int,
                     dislike: Int // нужно чтобы вычислить отношение лайков к дизлайкам
                    ) extends Product with Serializable


class NewsTable(tag: Tag) extends Table[NewsModel](tag, "news") {
  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def authorId = column[Int]("authorId")
  def canComment = column[Boolean]("canComment")
  def userCategory = column[UserCategory]("category")
    def filter = column[Filter]("filter")
  def importance = column[Importance]("importance")
  def date = column[Date]("date")
  def lifetime = column[Date]("lifetime")

  def content = column[String]("content")
  def titel = column[String]("titel")
  def sampleNumber = column[String]("sampleNumber")
  def img = column[String]("img")
  def like = column[Int]("likeCount")
  def dislike = column[Int]("dislikeCount")
  def * = (
    id,
    authorId,
    canComment,
    userCategory,
    filter,
    importance,
    date,
    lifetime,
    content,
    titel,
    sampleNumber,
    img,
    like,
    dislike
  ).mapTo[NewsModel]
}

