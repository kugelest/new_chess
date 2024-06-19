package htwg.se.chess

import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.server.Directives._
import scala.concurrent.{ExecutionContextExecutor, Future}

import scala.util.Failure
import scala.util.Success
import Console.{GREEN, RED, BLUE, RESET}
import scala.io.StdIn

import aview.HttpTui


object RootHttp {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[_] = ActorSystem(Behaviors.empty, "RootActor")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val client = new BoardHttpClient()(system)
    // val fileclient = new FileIoClient()(system)

    val routes: Route = concat(
      path("boards") {
        get {
          complete {
            client.getBoardsStr().map { response =>
              response
            }
          }
        }
      },
      path("board" / "create") {
        post {
          complete {
            client.createBoard().map { response =>
              response
            }
          }
        }
      },
      path("board" / IntNumber) { id =>
        get {
          complete {
            client.getBoardStr(id).map { response =>
              response
            }
          }
        }
      },
      path("board" / IntNumber / "move") { id =>
        put {
          entity(as[String]) { moveJson =>
            complete {
              client.execMove(id, moveJson).map { response =>
                response
              }
            }
          }
        }
      },
      path("save") {
        get {
          complete {
            client.save().map { response =>
              response
            }
          }
        }
      },
      path("load") {
        get {
          complete {
            client.load().map { response =>
              response
            }
          }
        }
      },
      // path("fileio" / "load") {
      //   get {
      //       complete {
      //         fileclient.load().map { response =>
      //           response
      //         }
      //       }
      //   }
      // },
      // path("fileio" / "save") {
      //   put {
      //     entity(as[String]) { boardJson =>
      //       complete {
      //         fileclient.save(boardJson).map { response =>
      //           response
      //         }
      //       }
      //     }
      //   }
      // },
    )

    startHttpServer(routes)

    HttpTui.run()

    // StdIn.readLine() // let it run until user presses return
    // system.terminate()

  }

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[?]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("0.0.0.0", 8090).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        val msg     = s"RootHttp-Server online at http://${address.getHostString}:${address.getPort}/"
        system.log.info(msg)
        Console.println(s"${BLUE}${msg}${RESET}")
      case Failure(ex)      =>
        val msg = s"Failed to bind HTTP endpoint, terminating system"
        system.log.error(msg, ex)
        Console.err.println(s"${RED}${msg}${RESET}")
        system.terminate()
    }
  }

}


