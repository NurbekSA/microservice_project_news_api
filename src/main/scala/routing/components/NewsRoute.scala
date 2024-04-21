package routing.components

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import model.enumClass.{Importance, UserCategory}
import repository.JsonSupport
import repository.newsComponent.NewsRepo
import model.newsComponents.NewsModel
import Main.Main.amqpActor
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import amqp._
import routing.handlers.MailSender
import scala.concurrent.duration._
class NewsRoute(implicit val system: ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher

  val route = pathPrefix("News") {
    pathEndOrSingleSlash {
      (get & parameters("field", "parameter")) { (field, parameter) =>
        onComplete(NewsRepo.getByField(field, parameter)) {
          case Success(queryResponse) => complete(StatusCodes.OK, queryResponse.toList)
          case Failure(_) => complete(StatusCodes.InternalServerError, "Failed to make the request!")
        }
      } ~
        get {
          onComplete(NewsRepo.getAll) {
            case Success(result) =>
              val newsList = result.toList
              complete(StatusCodes.OK, newsList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[NewsModel]) { newNews =>
            onComplete(NewsRepo.insertData(newNews)) {
              case Success(newNewsId) =>
                if(newNews.userCategory == UserCategory.teacher && newNews.importance == Importance.high){
                  implicit val timeout: Timeout = Timeout(5.seconds) // Например, 5 секунд
                  val result = amqpActor ? RabbitMQ.Ask("univer.Teacher_api.request"," ")
                  result.onComplete{
                    case Success(str:String) =>
                      MailSender.send(str)
                  }
                }
                complete(StatusCodes.Created, s"ID of the new news: ${newNewsId.toString}")
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Failed to create news!")
            }
          }
        }
    } ~
      path(IntNumber) { newsId =>
        get {
          onComplete(NewsRepo.getById(newsId)) {
            case Success(news) => complete(StatusCodes.OK, news)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
          put {
            entity(as[NewsModel]) { updatedNews =>
              onComplete(NewsRepo.updateData(updatedNews)) {
                case Success(_) => complete(StatusCodes.OK, "News updated successfully")
                case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
              }
            }
          } ~
          delete {
            onComplete(NewsRepo.deleteData(newsId)) {
              case Success(deletedNewsId) =>
                complete(StatusCodes.OK, s"Number of deleted rows: ${deletedNewsId.toString}")
              case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
            }
          }
      }
  }
}
