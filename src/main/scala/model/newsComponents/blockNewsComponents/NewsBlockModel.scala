package model.newsComponents.blockNewsComponents

import slick.jdbc.MySQLProfile.api._

case class NewsBlockModel(id: Option[Int], // блоки одной новости
                      nbpId: Int, // id основного блока к которому будут дополняться блоки
                      content: String,
                      contentType: String,
                      number: Int, // порядковый номер блока
                      css: String
                      )extends Product with Serializable

class NewsBlockTable(tag: Tag) extends Table[NewsBlockModel](tag, "NewsBlock") {
  def id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def nbpId: Rep[Int] = column[Int]("nbpId")
  def content: Rep[String] = column[String]("content")
  def contentType: Rep[String] = column[String]("contentType")
  def number: Rep[Int] = column[Int]("number")
  def css: Rep[String] = column[String]("css")

  def * = (id, nbpId, content, contentType, number, css).mapTo[NewsBlockModel]
}