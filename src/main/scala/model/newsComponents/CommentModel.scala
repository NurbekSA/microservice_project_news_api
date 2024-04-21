package model.newsComponents

import java.sql.Date
import slick.jdbc.MySQLProfile.api._
import model.enumClass._
import java.time.LocalDate

case class CommentModel(
                         id: Option[Int],
                         newsId: Int,
                         authorId: Int,
                         authorCategory: UserCategory,
                         newsCategory: NewsCategory,
                         date: Date,
                         content: String,
                         rating: Int
                       ) extends Product with Serializable


class CommentTable(tag: Tag) extends Table[CommentModel](tag, "comment") {
  def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def newsId = column[Int]("newsId")
  def authorId = column[Int]("authorId")
  def authorCategory = column[UserCategory]("authorCategory")
  def newsCategory = column[NewsCategory]("newsCategory")
  def date = column[Date]("date", O.Default(Date.valueOf(LocalDate.now())))
  def content = column[String]("content")
  def rating= column[Int]("rating", O.Default(0))

  def * = (id, newsId, authorId, authorCategory,newsCategory , date, content, rating).mapTo[CommentModel]
}
