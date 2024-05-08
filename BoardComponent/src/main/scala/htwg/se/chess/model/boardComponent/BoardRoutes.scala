package htwg.se.chess
package model
package boardComponent

import htwg.se.chess.model.boardComponent.BoardRegistry._
import htwg.se.chess.model.boardComponent.boardBaseImpl.Board
import org.apache.pekko

import scala.concurrent.Future

import pekko.http.scaladsl.server.Directives._
import pekko.http.scaladsl.model.StatusCodes
import pekko.http.scaladsl.server.Route
import pekko.actor.typed.ActorRef
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.AskPattern._
import pekko.util.Timeout

class BoardRoutes(boardRegistry: ActorRef[BoardRegistry.Command])(implicit val system: ActorSystem[?]) {

  import pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import BoardJsonFormats._

  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getBoards(): Future[Boards] =
    boardRegistry.ask(GetBoards.apply)

  def createBoard(): Future[ActionPerformed] =
    boardRegistry.ask(CreateBoard(_))

  // provide top-level path structure here but delegate functionality to subroutes for readability
  lazy val topLevelRoute: Route =
    concat(
      pathPrefix("boards")(boardsRoute),
      // extract URI path element as Long
      // pathPrefix("board" / LongNumber)(boardRoute),
      path("create")(createRoute)
    )

  lazy val boardsRoute: Route =
    pathEnd {
      concat(
        get {
          complete(getBoards())
        }
      )
    }

  // def boardRoute(boardId: Long): Route =
  //   concat(
  //     pathEnd {
  //       concat(
  //         get {
  //           complete(getBoard())
  //         }
  //       )
  //     }
  //   )

  lazy val createRoute: Route =
    concat(
      get {
        complete((StatusCodes.Created, createBoard()))
      }
    )
}
