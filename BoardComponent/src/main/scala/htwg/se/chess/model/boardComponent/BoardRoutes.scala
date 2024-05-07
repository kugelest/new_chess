package htwg.se.chess
package model
package boardComponent

import htwg.se.chess.model.boardComponent.BoardRegistry._
import htwg.se.chess.model.boardComponent.BoardInterface
import org.apache.pekko

import scala.concurrent.Future

import pekko.http.scaladsl.server.Directives._
import pekko.http.scaladsl.model.StatusCodes
import pekko.http.scaladsl.server.Route
import pekko.actor.typed.ActorRef
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.AskPattern._
import pekko.util.Timeout

class BoardRoutes(boardRegistry: ActorRef[BoardRegistry.Command])(implicit val system: ActorSystem[_]) {

  import pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import BoardJsonFormats._

  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getBoards(): Future[Boards] =
    boardRegistry.ask(GetBoards.apply)

  val boardRoutes: Route =
    pathPrefix("boards") {
      concat(
        pathEnd {
          concat(
            get {
              complete(getBoards())
            }
          )
        }
      )
    }
}
