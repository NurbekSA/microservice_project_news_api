package Main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.json4s.{DefaultFormats, jackson}
import scala.concurrent.{ExecutionContextExecutor}
import scala.io.StdIn
import amqp.AmqpActor
import akka.actor.{Props}
import routing._
import routing.components._
import routing.components.blockNewsComponents._
import repository._
import repository.newsComponent._
import repository.newsComponent.blockNewsComponents._
import akka.http.scaladsl.server.Directives._
import slick.jdbc.MySQLProfile.api.{Database}


object DatabaseManager {
  val db: Database = Database.forConfig("mysqlDB")
}

object Main{

  // Извлечение значения параметра serviceName
  val serviceName = "News_api"


  implicit val system: ActorSystem = ActorSystem("web-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats


  val amqpActor = system.actorOf(Props(new AmqpActor("X:routing.topic",serviceName)),"amqpActor")

  private val nbpRoute = new NewsBlockPrimeRoute()
  private val nbRoute:NewsBlockRoute = new NewsBlockRoute()
  private val commentRoute:CommentsRoute = new CommentsRoute()
  private val newstRoute:NewsRoute = new NewsRoute()
  private val nnRoute:NotifyingNewsRoute = new NotifyingNewsRoute()
  private val naRoute:NewsAssemblyRoute = new NewsAssemblyRoute()
  private val nbaRoute:NewsBlockAssemblyRoute = new NewsBlockAssemblyRoute()
  private val nnaRoute:NotifyingNewsAssemblyRoute = new NotifyingNewsAssemblyRoute()

  def main(args: Array[String]): Unit = {



    val allRoutes =
      nbpRoute.route ~
        nbRoute.route ~
        commentRoute.route ~
        newstRoute.route ~
        nnRoute.route ~
        naRoute.route ~
        nbaRoute.route ~
        nnaRoute.route;

    val bindingFuture = Http().bindAndHandle(allRoutes, "0000", 8080)
    println(s"Server online at http://localhost:8080  /\nPress RETURN to stop...")

    sys.addShutdownHook(
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    )

  }
}
