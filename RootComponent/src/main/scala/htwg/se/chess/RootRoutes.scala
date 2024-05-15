// package htwg.se.chess
//
// import org.apache.pekko.http.scaladsl.client.RequestBuilding.Get
// import org.apache.pekko.http.scaladsl.client.RequestBuilding.Post
// import org.apache.pekko
//
// import scala.concurrent.Future
//
// import pekko.http.scaladsl.server.Directives._
// import pekko.http.scaladsl.model.StatusCodes
// import pekko.http.scaladsl.server.Route
// import pekko.actor.typed.ActorRef
// import pekko.actor.typed.ActorSystem
// import pekko.actor.typed.scaladsl.AskPattern._
// import pekko.util.Timeout
// import spray.json._
//
//
// class RootRoutes()(implicit val system: ActorSystem[?]) {
//
//   import pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
//   // import BoardJsonFormats._
//   import DefaultJsonProtocol._
//
//   private implicit val timeout: Timeout = Timeout.create(
//     system.settings.config.getDuration("my-app.routes.ask-timeout")
//   )
//
//   // def getBoards(): Future[Boards] =
//   //   boardRegistry.ask(GetBoards.apply)
//   //
//   // def getBoard(id: Int): Future[GetBoardResponse] =
//   //   boardRegistry.ask(GetBoard(id, _))
//
//   def createBoard() =
//     Post("http://localhost:8081/board/create")
//
//
// //   def execMove(id: Int, move: Move): Future[ActionPerformed] =
// //     boardRegistry.ask(ExecMove(id, move, _))
// //
//   lazy val topLevelRoute: Route =
//     concat(
//       path("board" / "create") {
//         post {
//           complete(createBoard())
//         }
//       }
//     )
// }
// //
// //   lazy val boardsRoute: Route =
// //     pathEnd {
// //       get {
// //         onSuccess(createBoard()) { response =>
// //           complete(response)
// //         }
// //       }
// //     }
// //
// //   lazy val boardRoute: Route =
// //     path("create") {
// //       post {
// //         onSuccess(createBoard()) { response =>
// //           complete((StatusCodes.Created, response))
// //         }
// //       }
// //     }
// //
// //   def boardIdRoute(id: Int): Route =
// //     pathEnd {
// //       get {
// //         rejectEmptyResponse {
// //           onSuccess(getBoard(id)) { response =>
// //             complete(response.maybeBoard)
// //           }
// //         }
// //       }
// //     } ~
// //       path("move") {
// //         concat(
// //           put {
// //             entity(as[Move]) { m =>
// //               onSuccess(execMove(id, m)) { response =>
// //                 complete((StatusCodes.OK, response))
// //               }
// //             }
// //           }
// //         )
// //       }
// //
// // }
