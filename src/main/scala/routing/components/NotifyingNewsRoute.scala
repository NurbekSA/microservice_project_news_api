package routing.components

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import repository.{JsonSupport}
import repository.newsComponent.NotifyingNewsRepo
import model.newsComponents.NotifyingNewsModel
import scala.concurrent.{ExecutionContext}
import scala.util.{Failure, Success}

class NotifyingNewsRoute(implicit val system: ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher

  val route = pathPrefix("NotifyingNews") {
    pathEndOrSingleSlash {
      (get & parameters("field", "parameter")) { (field, parameter) =>
        onComplete(NotifyingNewsRepo.getByField(field, parameter)) {
          case Success(queryResponse) => complete(StatusCodes.OK, queryResponse.toList)
          case Failure(_) => complete(StatusCodes.InternalServerError, "Failed to make the request!")
        }
      } ~
        get {
          onComplete(NotifyingNewsRepo.getAll) {
            case Success(result) =>
              val notifyingNewsList = result.toList
              complete(StatusCodes.OK, notifyingNewsList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[NotifyingNewsModel]) { newNotifyingNews =>
            onComplete(NotifyingNewsRepo.insertData(newNotifyingNews)) {
              case Success(newNotifyingNewsId) =>
                complete(StatusCodes.Created, s"ID of the new notifying news: ${newNotifyingNewsId.toString}")
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Failed to create notifying news!")
            }
          }
        }
    } ~
      path(IntNumber) { notifyingNewsId =>
        get {
          onComplete(NotifyingNewsRepo.getById(notifyingNewsId)) {
            case Success(notifyingNews) => complete(StatusCodes.OK, notifyingNews)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
          put {
            entity(as[NotifyingNewsModel]) { updatedNotifyingNews =>
              onComplete(NotifyingNewsRepo.updateData(updatedNotifyingNews)) {
                case Success(_) => complete(StatusCodes.OK, "Notifying news updated successfully")
                case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
              }
            }
          } ~
          delete {
            onComplete(NotifyingNewsRepo.deleteData(notifyingNewsId)) {
              case Success(deletedNotifyingNewsId) =>
                complete(StatusCodes.OK, s"Number of deleted rows: ${deletedNotifyingNewsId.toString}")
              case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
            }
          }
      }
  }
}
