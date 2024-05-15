package htwg.se.chess
package model
package boardComponent

import htwg.se.chess.model.boardComponent.BoardRegistry._
import htwg.se.chess.model.boardComponent.boardBaseImpl.Board
import htwg.se.chess.model.boardComponent.boardBaseImpl.Move
import htwg.se.chess.model.boardComponent.boardBaseImpl.Coord
import org.apache.pekko

import scala.concurrent.Future

import pekko.http.scaladsl.server.Directives._
import pekko.http.scaladsl.model.StatusCodes
import pekko.http.scaladsl.server.Route
import pekko.actor.typed.ActorRef
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.AskPattern._
import pekko.util.Timeout
import spray.json._

class BoardRoutes(boardRegistry: ActorRef[BoardRegistry.Command])(implicit val system: ActorSystem[?]) {

  import pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import BoardJsonFormats._
  import DefaultJsonProtocol._

  private implicit val timeout: Timeout = Timeout.create(
    system.settings.config.getDuration("my-app.routes.ask-timeout")
  )

  def getBoards(): Future[Boards] =
    boardRegistry.ask(GetBoards.apply)

  def getBoard(id: Int): Future[GetBoardResponse] =
    boardRegistry.ask(GetBoard(id, _))

  def createBoard(): Future[ActionPerformed] =
    boardRegistry.ask(CreateBoard(_))

  def execMove(id: Int, move: Move): Future[ActionPerformed] =
    boardRegistry.ask(ExecMove(id, move, _))

  lazy val topLevelRoute: Route =
    concat(
      pathPrefix("boards")(boardsRoute),
      pathPrefix("board")(boardRoute),
      pathPrefix("board" / IntNumber)(boardIdRoute)
    )

  lazy val boardsRoute: Route =
    pathEnd {
      get {
        onSuccess(getBoards()) { response =>
          complete(response)
        }
      }
    }

  lazy val boardRoute: Route =
    path("create") {
      post {
        onSuccess(createBoard()) { response =>
          complete((StatusCodes.Created, response))
        }
      }
    }

  def boardIdRoute(id: Int): Route =
    pathEnd {
      get {
        rejectEmptyResponse {
          onSuccess(getBoard(id)) { response =>
            complete(response.maybeBoard)
          }
        }
      }
    } ~
      path("move") {
        concat(
          put {
            entity(as[Move]) { m =>
              onSuccess(execMove(id, m)) { response =>
                complete((StatusCodes.OK, response))
              }
            }
          }
        )
      }

}
