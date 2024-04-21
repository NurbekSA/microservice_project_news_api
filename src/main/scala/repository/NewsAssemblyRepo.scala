package repository

import model.NewsAssemblyModel
import model.newsComponents._
import repository.newsComponent._
import repository.newsComponent.blockNewsComponents._

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import model.enumClass
import model.enumClass.NewsCategory

object NewsAssemblyRepo {

  def getAll(): Future[List[NewsAssemblyModel]] = {
    val resultFuture: Future[List[NewsAssemblyModel]] = for {
      news <- NewsRepo.getAll()
      comment <- CommentRepo.getAll()
    } yield {
      println("get all started")
      val newsAssemblyList = ListBuffer.empty[NewsAssemblyModel]
      var commentsList = ListBuffer.empty[CommentModel]

      news.foreach(news => {
        comment.filter(com => news.id == com.newsId & com.newsCategory == NewsCategory.base).foreach(com => {
          commentsList.addOne(com)
        })
        val newsAssembly = NewsAssemblyModel(news, commentsList.toList)
        newsAssemblyList.addOne(newsAssembly)
        commentsList = ListBuffer.empty[CommentModel]
      })
      newsAssemblyList.toList
    }
    resultFuture
  }

  def insert(newsAssembly: NewsAssemblyModel): Future[Unit] = {
    newsAssembly.comments.foreach(CommentRepo.insertData)
    NewsRepo.insertData(newsAssembly.news)
    Future.unit
  }

  def deleteData(id: Int): Future[Unit] = {
    getById(id).map(x => {
      x.comments.foreach(y => CommentRepo.deleteData(y.id.get))
      NewsRepo.deleteData(x.news.id.get)
    })
  }
  def getById(id: Int): Future[NewsAssemblyModel] = {
    val comment = CommentRepo.getAll().map(x => {
      x.filter(y => y.newsId == id && y.newsCategory == NewsCategory.base)
    })
    for{
      news <- NewsRepo.getById(id)
      comm <- comment

    }yield {
      NewsAssemblyModel(news(0), comm.toList)
    }
  }


}
