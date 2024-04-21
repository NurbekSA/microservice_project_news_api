package routing

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import repository.{BlockNewsAssemblyRepo, JsonSupport}
import scala.concurrent.{ExecutionContext}
import scala.util.{Failure, Success}
import repository.BlockNewsAssemblyRepo
import model.BlockNewsAssemblyModel

class NewsBlockAssemblyRoute(implicit val system: ActorSystem) extends JsonSupport {

  implicit val executionContext: ExecutionContext = system.dispatcher

  val route = pathPrefix("BlockNewsAssembly") {
    pathEndOrSingleSlash {
        get {
          onComplete(BlockNewsAssemblyRepo.getAll) {
            case Success(result) =>
              val blockNewsAssemblyList = result.toList
              complete(StatusCodes.OK, blockNewsAssemblyList)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        post {
          entity(as[BlockNewsAssemblyModel]) { newBlockNewsAssembly =>
            onComplete(BlockNewsAssemblyRepo.insert(newBlockNewsAssembly)) {
              case Success(newBlockNewsAssemblyId) =>
                complete(StatusCodes.Created, s"ID of the new block news assembly: ${newBlockNewsAssemblyId.toString}")
              case Failure(_) =>
                complete(StatusCodes.InternalServerError, "Failed to create block news assembly!")
            }
          }
        }
    } ~
      path(IntNumber) { blockNewsAssemblyId =>
        get {
          onComplete(BlockNewsAssemblyRepo.getById(blockNewsAssemblyId)) {
            case Success(blockNewsAssembly) => complete(StatusCodes.OK, blockNewsAssembly)
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        } ~
        delete {
          onComplete(BlockNewsAssemblyRepo.deleteData(blockNewsAssemblyId)) {
            case Success(deletedBlockNewsAssemblyId) =>
              complete(StatusCodes.OK, s"Number of deleted rows: ${deletedBlockNewsAssemblyId.toString}")
            case Failure(ex) => complete(StatusCodes.NotFound, s"Error in code: ${ex.getMessage}")
          }
        }
      }
  }
}
