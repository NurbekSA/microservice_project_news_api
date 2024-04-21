package routing.components

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import model.newsComponents._
import repository.{NewsAssemblyRepo, JsonSupport}
import repository.newsComponent._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

class CommentsRoute(implicit val system:ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher
  val amqpActor = system.actorSelection("user/amqpActor")

  val route = pathPrefix("Comment") {
    pathEndOrSingleSlash {
      (get & parameters("field", "parameter")) {
        (field, parameter) => {
          onComplete(CommentRepo.getByField(field, parameter)) {
            case Success(queryResponse) => complete(StatusCodes.OK, queryResponse.toList)
            case Failure(_) => complete(StatusCodes.InternalServerError, "Не удалось сделать запрос!")
          }
        }
      } ~
        get {
          onComplete(CommentRepo.getAll) {
            case Success(result) =>
              val commentList = result.toList
              complete(StatusCodes.OK, commentList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Ошибка в коде: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[CommentModel]) {
            newComment => {
              onComplete(CommentRepo.insertData(newComment)) {
                case Success(newCommentId) =>
                  complete(StatusCodes.Created, s"ID нового коментария ${newCommentId.toString}")
                case Failure(_) =>
                  complete(StatusCodes.InternalServerError, "Не удалось создать коментарий!")
              }
            }
          }
        }
    } ~
      path(IntNumber) { commentId =>
        get {
          onComplete(CommentRepo.getById(commentId)) {
            case Success(comment) => complete(StatusCodes.OK, comment)
            case Success(None) => complete(StatusCodes.BadRequest,s"Коментарий под айди $commentId не существует!")
            case Failure(ex) => complete(StatusCodes.NotFound, s"Ошибка в коде: ${ex.getMessage}")
          }
        } ~
          put {
            entity(as[CommentModel]) {
              updatedComment => {
                  onComplete(CommentRepo.updateData(updatedComment)) {
                    case Success(updatedCommentId) => complete(StatusCodes.OK, updatedCommentId.toString)
                    case Failure(ex) => complete(StatusCodes.NotFound, s"Ошибка в коде: ${ex.getMessage}")
                }
              }
            }
          } ~
          delete {
            onComplete(CommentRepo.deleteData(commentId)) {
              case Success(deletedCommentId) =>
                complete(StatusCodes.OK, s"Число удаленных строк ${deletedCommentId.toString}")
              case Failure(ex) => complete(StatusCodes.NotFound, s"Ошибка в коде:${ex.getMessage}")
            }
          }
      }
  }
}