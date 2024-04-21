package model.newsComponents

import model.enumClass.UserCategory
import slick.jdbc.MySQLProfile.api._

import java.sql.Date

case class NotifyingNewsModel(
                               id: Option[Int],
                               authorId: Int,
                               content: String,
                               title: String,
                               date:   Date
                             ) extends Product with Serializable

class NotifyingNewsTable(tag: Tag) extends Table[NotifyingNewsModel](tag, "notifyingNews") {
  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def authorId = column[Int]("authorId")
  def content = column[String]("content")
  def title = column[String]("title") // Corrected spelling to 'title'
  def date = column[Date]("date") // Removed O.Default modifier
  def * = (id, authorId, content, title, date).mapTo[NotifyingNewsModel]
}
