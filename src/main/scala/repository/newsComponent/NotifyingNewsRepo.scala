package repository.newsComponent

import Main._
import model.newsComponents.{NotifyingNewsModel, NotifyingNewsTable}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

object NotifyingNewsRepo {
  // Определение запроса к таблице новостей
  val NotifytedNewsQuery: TableQuery[NotifyingNewsTable] = TableQuery[NotifyingNewsTable]

  // Метод для вставки данных новости в базу данных
  def insertData(data: NotifyingNewsModel): Future[Int] = {
    // Создание действия вставки
    val insertAction = NotifytedNewsQuery += data
    // Запуск действия вставки в базу данных и возврат фьючера с результатом
    DatabaseManager.db.run(insertAction)
  }

  def deleteData(id: Int): Future[Int] = {
    val deleteAction = NotifytedNewsQuery.filter(_.id === id.toInt).delete
    DatabaseManager.db.run(deleteAction)
  }

  def updateData(updatedData: NotifyingNewsModel): Future[Int] = {
    val updateAction = NotifytedNewsQuery.filter(_.id === updatedData.id)
      .map(notifytedNews => (notifytedNews.authorId, notifytedNews.content, notifytedNews.date, notifytedNews.title))
      .update((updatedData.authorId, updatedData.content, updatedData.date, updatedData.title))
    DatabaseManager.db.run(updateAction)
  }

  def getAll(): Future[Seq[NotifyingNewsModel]] = {
    val query = NotifytedNewsQuery.result
    DatabaseManager.db.run(query)
  }

  def getById(id: Int): Future[Seq[NotifyingNewsModel]] = {
    val query = NotifytedNewsQuery.filter(_.id === id.toInt).result
    DatabaseManager.db.run(query)
  }

  def getByField(fieldName: String, value: String): Future[Seq[NotifyingNewsModel]] = {
    val query = NotifytedNewsQuery.filter(table => table.column[String](fieldName) === value).result
    DatabaseManager.db.run(query)
  }
}