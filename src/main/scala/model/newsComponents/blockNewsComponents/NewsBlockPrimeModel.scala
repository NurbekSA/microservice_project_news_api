package model.newsComponents.blockNewsComponents

import model.enumClass.{Filter, Importance, UserCategory}
import slick.jdbc.MySQLProfile.api._

import java.sql.Date


case class NewsBlockPrimeModel(id: Option[Int],
                     authorId: Int,
                     canComment: Boolean,
                     authorCategory: UserCategory,
                     date: Date,
                     filter: Filter,
                     importance: Importance,
                     lifetime: Date,
                     titel: String,
                     like: Int,
                     dislike: Int // нужно чтобы вычислить отношение лайков к дизлайкам
                       ) extends Product with Serializable


class NewsBlockPrimeTable(tag: Tag) extends Table[NewsBlockPrimeModel](tag, "blockNews") {
  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def authorId = column[Int]("authorId")
  def canComment = column[Boolean]("canComment")
  def authorCategory = column[UserCategory]("category")
  def date = column[Date]("date")
  def filter = column[Filter]("filter")
  def importance = column[Importance]("importance")
  def lifetime = column[Date]("lifetime")
  def titel = column[String]("titel")

  def like = column[Int]("likeCount")
  def dislike = column[Int]("dislikeCount")
  def * = (
    id,
    authorId,
    canComment,
    authorCategory,
    date,
    filter,
    importance,
    lifetime,
    titel,
    like,
    dislike

  ).mapTo[NewsBlockPrimeModel]
}

