package repository.newsComponent.blockNewsComponents

import Main._
import model.newsComponents.blockNewsComponents.{NewsBlockModel, NewsBlockTable}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

import model._


object NewsBlockRepo {
  // Определение запроса к таблице блоков новостей
  val NewsBlockQuery: TableQuery[NewsBlockTable] = TableQuery[NewsBlockTable]

  // Метод для вставки данных блока новости в базу данных
  def insertData(data: NewsBlockModel): Future[Int] = {
    // Создание действия вставки
    val insertAction = NewsBlockQuery += data
    // Запуск действия вставки в базу данных и возврат фьючера с результатом
    DatabaseManager.db.run(insertAction)
  }

  // Метод для удаления данных блока новости из базы данных по идентификатору
  def deleteData(id: Int): Future[Int] = {
    val deleteAction = NewsBlockQuery.filter(_.id === id.toInt).delete
    DatabaseManager.db.run(deleteAction)
  }

  // Метод для обновления данных блока новости в базе данных
  def updateData(updatedData: NewsBlockModel): Future[Int] = {
    val updateAction = NewsBlockQuery.filter(_.id === updatedData.id)
      .map(newsBlock => (newsBlock.nbpId, newsBlock.content, newsBlock.contentType, newsBlock.number, newsBlock.css))
      .update((updatedData.nbpId, updatedData.content, updatedData.contentType, updatedData.number, updatedData.css))
    DatabaseManager.db.run(updateAction)
  }

  // Метод для получения всех блоков новостей из базы данных
  def getAll(): Future[Seq[NewsBlockModel]] = {
    val query = NewsBlockQuery.result
    DatabaseManager.db.run(query)
  }

  // Метод для получения блока новости из базы данных по идентификатору
  def getById(id: Int): Future[Option[NewsBlockModel]] = {
    val query = NewsBlockQuery.filter(_.id === id.toInt).result.headOption
    DatabaseManager.db.run(query)
  }

  // Метод для получения блока новости из базы данных по заданному полю и значению
  def getByField(fieldName: String, value: String): Future[Seq[NewsBlockModel]] = {
    val query = NewsBlockQuery.filter(table => table.column[String](fieldName) === value).result
    DatabaseManager.db.run(query)
  }
}
