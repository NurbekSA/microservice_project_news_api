package routing.components.blockNewsComponents

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import repository.{JsonSupport}
import repository.newsComponent.blockNewsComponents.NewsBlockRepo
import model.newsComponents.blockNewsComponents.NewsBlockModel
import scala.concurrent.{ExecutionContext}
import scala.util.{Failure, Success}

class NewsBlockRoute(implicit val system: ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher

  val route = pathPrefix("Block") {
    pathEndOrSingleSlash {
      (get & parameters("field", "parameter")) { (field, parameter) =>
        onComplete(NewsBlockRepo.getByField(field, parameter)) {
          case Success(queryResponse) => complete(StatusCodes.OK, queryResponse.toList)
          case Failure(_) => complete(StatusCodes.InternalServerError, "Failed to make the request!")
        }
      } ~
        get {
          onComplete(NewsBlockRepo.getAll) {
            case Success(result) =>
              val newsBlockList = result.toList
              complete(StatusCodes.OK, newsBlockList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[NewsBlockModel]) { newNewsBlock =>
            onComplete(NewsBlockRepo.insertData(newNewsBlock)) {
              case Success(newNewsBlockId) =>
                complete(StatusCodes.Created, s"ID of the new news block: ${newNewsBlockId.toString}")
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Failed to create news block!")
            }
          }
        }
    } ~
      path(IntNumber) { newsBlockId =>
        get {
          onComplete(NewsBlockRepo.getById(newsBlockId)) {
            case Success(newsBlock) => complete(StatusCodes.OK, newsBlock)
            case Success(None) => complete(StatusCodes.BadRequest, s"News block with ID $newsBlockId does not exist!")
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
          put {
            entity(as[NewsBlockModel]) { updatedNewsBlock =>
              onComplete(NewsBlockRepo.updateData(updatedNewsBlock)) {
                case Success(_) => complete(StatusCodes.OK, "News block updated successfully")
                case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
              }
            }
          } ~
          delete {
            onComplete(NewsBlockRepo.deleteData(newsBlockId)) {
              case Success(deletedNewsBlockId) =>
                complete(StatusCodes.OK, s"Number of deleted rows: ${deletedNewsBlockId.toString}")
              case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
            }
          }
      }
  }
}
