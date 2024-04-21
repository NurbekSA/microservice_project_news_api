package repository

import model.BlockNewsAssemblyModel
import model.newsComponents.CommentModel
import repository.newsComponent.CommentRepo
import model.newsComponents.blockNewsComponents._
import repository.newsComponent.blockNewsComponents._

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import model.enumClass
import model.enumClass.NewsCategory
object BlockNewsAssemblyRepo {

  def getAll(): Future[List[BlockNewsAssemblyModel]] = {
    val resultFuture: Future[List[BlockNewsAssemblyModel]] = for {
      blockPrime <- NewsBlockPrimeRepo.getAll()
      block <- NewsBlockRepo.getAll()
      comment <- CommentRepo.getAll()
    } yield {
      println("get all started")
      val bnam = ListBuffer.empty[BlockNewsAssemblyModel]
      var cm = ListBuffer.empty[CommentModel]
      var bm = ListBuffer.empty[NewsBlockModel]
      blockPrime.foreach(news => {
        comment.filter(com => news.id == com.newsId & com.newsCategory == NewsCategory.block).foreach(com => {
          cm.addOne(com)
        })
        block.filter(bk => news.id == bk.nbpId).foreach(bk => {
          bm.addOne(bk)
        })
        val na = BlockNewsAssemblyModel(news,  bm.toList, cm.toList)
        bnam.addOne(na)
        cm = ListBuffer.empty[CommentModel]
        bm = ListBuffer.empty[NewsBlockModel]
      })
      bnam.toList
    }
    resultFuture
  }

  def insert(blockNews: BlockNewsAssemblyModel): Future[Unit] = {
    blockNews.blocks.foreach(NewsBlockRepo.insertData)
    blockNews.comments.foreach(CommentRepo.insertData)
    NewsBlockPrimeRepo.insertData(blockNews.primeBlock)
    Future.unit
  }

  def deleteData(id: Int): Future[Unit] = {
    getById(id).map(x => {
      x.comments.foreach(y => CommentRepo.deleteData(y.id.get))
      x.blocks.foreach(y => NewsBlockRepo.deleteData(y.id.get))
      NewsBlockPrimeRepo.deleteData(x.primeBlock.id.get)
    })
  }

  def getById(id: Int): Future[BlockNewsAssemblyModel] = {
    val comment = CommentRepo.getAll().map(x => {
      x.filter(y => y.newsId == id && y.newsCategory == NewsCategory.block)
    })
    for {
      blockrime <- NewsBlockPrimeRepo.getById(id)
      block <- NewsBlockRepo.getAll()
      comm <- comment

    } yield {

      BlockNewsAssemblyModel(blockrime.get, block.filter(y => y.nbpId == id).toList,comm.toList)
    }
  }
}