package routing

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import repository.NotifyingNewsAssemblyRepo
import model.NotifyingNewsAssemblyModel
import repository.{JsonSupport}
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

class   NotifyingNewsAssemblyRoute(implicit val system:ActorSystem)  extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher
  val amqpActor = system.actorSelection("user/amqpActor")

  val route = pathPrefix("NotifyingNewsAssembly") {
    pathEndOrSingleSlash {
      get {
        onComplete(NotifyingNewsAssemblyRepo.getAll) {
          case Success(result) =>
            complete(StatusCodes.OK, result)
          case Failure(ex) => complete(StatusCodes.NotFound, s"Ошибка в коде: ${ex.getMessage}")
        }
      } ~
        post {
          entity(as[NotifyingNewsAssemblyModel]) { style =>
            onComplete(NotifyingNewsAssemblyRepo.insertData(style)) {
              case Success(newCourseId) =>
                complete(StatusCodes.Created, s"ID нового курса: $newCourseId")
              case Failure(ex) =>
                complete(StatusCodes.InternalServerError, s"Не удалось создать курс: ${ex.getMessage}")
            }
          }
        }
    }
  }
}