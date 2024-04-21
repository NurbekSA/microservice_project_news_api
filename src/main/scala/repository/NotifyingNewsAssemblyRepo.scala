package repository

import Main._
import model.newsComponents.{CommentModel, NotifyingNewsModel}
import model.NotifyingNewsAssemblyModel
import model.enumClass.NewsCategory
import repository.newsComponent.{CommentRepo, NotifyingNewsRepo}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ListBuffer


object NotifyingNewsAssemblyRepo {

  def getAll(): Future[List[NotifyingNewsAssemblyModel]] = {
    val resultFuture: Future[List[NotifyingNewsAssemblyModel]] = for {
      comment <- CommentRepo.getAll()
      notifying <- NotifyingNewsRepo.getAll()
    } yield {
      println("get all started")
      val lnna = ListBuffer.empty[NotifyingNewsAssemblyModel]
      var lb = ListBuffer.empty[CommentModel]
      notifying.foreach(news => {
        comment.filter(com => news.id == com.newsId && com.newsCategory == NewsCategory.notifying).foreach(com => {
          println("this matching id and newsIs")
          lb.addOne(com)
        })

        val na = NotifyingNewsAssemblyModel(news, lb.toList)
        lnna.addOne(na)
        lb = ListBuffer.empty[CommentModel]
      })
      lnna.toList
    }
    resultFuture
  }

  def insertData(nna: NotifyingNewsAssemblyModel): Future[Unit] = Future{
    println("whoohoohoohoo")
    println(nna.news.toString)
    nna.comments.foreach(x => {
      println(x.toString)
      CommentRepo.insertData(x)
    })
    NotifyingNewsRepo.insertData(nna.news)
  }


  def getById(id: Int): Future[NotifyingNewsAssemblyModel] = {
    val comment = CommentRepo.getAll().map(x => {
      x.filter(y => y.newsId == id && y.newsCategory == NewsCategory.notifying)
    })
    for {
      notify <- NotifyingNewsRepo.getById(id)
      comm <- comment

    } yield {
      NotifyingNewsAssemblyModel(notify(0), comm.toList)
    }
  }


}