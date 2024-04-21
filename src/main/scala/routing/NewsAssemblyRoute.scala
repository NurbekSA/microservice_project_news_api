package routing

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import repository.{JsonSupport, NewsAssemblyRepo}
import scala.concurrent.{ExecutionContext}
import scala.util.{Failure, Success}
import repository.NewsAssemblyRepo
import model.NewsAssemblyModel

class NewsAssemblyRoute(implicit val system: ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher

  val route = pathPrefix("NewsNssembly") {
    pathEndOrSingleSlash {
        get {
          onComplete(NewsAssemblyRepo.getAll) {
            case Success(result) =>
              val newsAssemblyList = result.toList
              complete(StatusCodes.OK, newsAssemblyList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[NewsAssemblyModel]) { newNewsAssembly =>
            onComplete(NewsAssemblyRepo.insert(newNewsAssembly)) {
              case Success(newNewsAssemblyId) =>
                complete(StatusCodes.Created, s"ID of the new news assembly: ${newNewsAssemblyId.toString}")
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Failed to create news assembly!")
            }
          }
        }
    } ~
      path(IntNumber) { newsAssemblyId =>
        get {
          onComplete(NewsAssemblyRepo.getById(newsAssemblyId)) {
            case Success(newsAssembly) => complete(StatusCodes.OK, newsAssembly)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        }~
        delete {
          onComplete(NewsAssemblyRepo.deleteData(newsAssemblyId)) {
            case Success(deletedNewsAssemblyId) =>
              complete(StatusCodes.OK, s"Number of deleted rows: ${deletedNewsAssemblyId.toString}")
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        }
      }
  }
}
