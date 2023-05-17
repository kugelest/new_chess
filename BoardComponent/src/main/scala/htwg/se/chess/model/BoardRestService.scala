package htwg.se.chess.model
package BoardComponent

import htwg.se.chess.model.BoardComponent.boardBaseImpl.Board

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
object BoardRestService {

  // def main(args: Array[String]): Unit = {
  // val injector = Guice.createInjector(new ChessModule)
  @main def main = {
    val board: BoardInterface = Board().startPos()

    implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "BoardRestService")
    // implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val route: Route =
      concat(
        get {
          pathSingleSlash {
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Chess</h1>"))
          }
          path("board") {
            complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("Board").toString))
          } ~
            path("board" / "turn") {
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(board.turn.toString).toString))
            } ~
            path("board" / "is_move_valid") {
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(board.isMoveValid("a2", "a4")).toString))
            } ~
            path("board" / "to_html") {
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(board.toHtml()).toString))
            } ~
            path("board" / "to_json") {
              complete(HttpEntity(ContentTypes.`application/json`, Json.toJson(board.toHtml()).toString))
            }
        },
        post {
          pathSingleSlash {
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Chess</h1>"))
          }
          path("board") {
            complete(HttpEntity(ContentTypes.`application/json`, Json.toJson("Board").toString))
          } ~
            path("board" / "do_move") {
              complete(
                HttpEntity(ContentTypes.`application/json`, Json.toJson(board.doMove("a2", "a4").toJson).toString)
              )
            } ~
            path("board" / "undo_move") {
              complete(
                HttpEntity(ContentTypes.`application/json`, Json.toJson(board.undoMove("a4", "a2").toJson).toString)
              )
            }

        }
      )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    def unbind = {
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
    }

  }
}
