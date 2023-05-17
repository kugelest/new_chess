package htwg.se.chess.model
package FileIOComponent

// import htwg.se.chess.model.FileIOComponent.fileIoJsonImpl
import htwg.se.chess.model.FileIOComponent.fileIoJsonImpl.FileIO

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.Route
import scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor
import com.google.inject.Guice
import play.api.libs.json.Json

// def squares: Vector[SquareInterface]
// def turn: PieceColor
//
// def startPos(): BoardInterface
// def isMoveValid(from: String, to: String): Boolean
// def doMove(from: String, to: String): BoardInterface
// def undoMove(from: String, to: String): BoardInterface
// def toJson: JsValue
// def toHtml(): String
object FileIoRestService {

  // def main(args: Array[String]): Unit = {
  // val injector = Guice.createInjector(new ChessModule)
  @main def main = {
    val file_io: FileIOInterface = FileIO()

    implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "FileiORestService")
    // implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val route: Route = get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>File IO</h1>"))
      }
      path("file_io") {
        complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("Board").toString))
      } ~
        path("file_io" / "load") {
          complete(
            HttpEntity(ContentTypes.`application/json`, Json.toJson(file_io.load.map(_.map(_.toJson)).get.get).toString)
          )
        } ~
        path("file_io" / "save") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("save").toString))
        } ~
        path("file_io" / "unbind") {
          complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("unbind").toString))
        }
    }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    def unbind = {
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }

  }
}
