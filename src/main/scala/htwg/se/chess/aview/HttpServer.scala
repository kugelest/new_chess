package htwg.se.chess.aview

import htwg.se.chess.controller.Controller
import htwg.se.chess.model.BoardComponent.boardBaseImpl.Move

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.Route
import scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor

class HttpServer(controller: Controller) {

  // def main(args: Array[String]): Unit = {

  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
  // implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val route: Route = get {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>HTWG Chess</h1>"))
    }
    path("chess") {
      boardToHtml
    } ~
      path("chess" / "start") {
        controller.doAndPublish(controller.newGame)
        boardToHtml
      } ~
      path("chess" / "load") {
        controller.doAndPublish(controller.load)
        boardToHtml
      } ~
      path("chess" / "save") {
        controller.save
        boardToHtml
      } ~
      path("chess" / "undo") {
        controller.doAndPublish(controller.undo)
        boardToHtml
      } ~
      path("chess" / "redo") {
        controller.doAndPublish(controller.redo)
        boardToHtml
      } ~
      path("chess" / Segment) { command =>
        {
          processInputLine(command)
          boardToHtml
        }
      }
  }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  def unbind = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def boardToHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Chess</h1>" + controller.boardToHtml))
  }

  val movePattern = """move (\w{2}) (\w{2})""".r

  def processInputLine(input: String): Unit = {
    input match {
      case movePattern(from, to) => controller.doAndPublish(controller.makeMove, Move(from, to))
      case _                     =>
    }
  }

}
