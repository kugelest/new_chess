package htwg.se.chess
package model
package fileIOComponent

import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.server.Route
import org.apache.pekko.http.scaladsl.server.Directives._
import scala.concurrent.{ExecutionContextExecutor, Future}

import scala.util.{Try, Success, Failure}
import Console.{GREEN, RED, BLUE, RESET}
import scala.io.StdIn

import fileIoJsonImpl._


object FileIoHttpServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[_] = ActorSystem(Behaviors.empty, "FileIoActor")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val routes: Route = concat(
      path("fileio" / "load") {
        get {
          complete {
            FileIO.load
          }
        }
      },
      path("fileio" / "save") {
        put {
          entity(as[String]) { boardJson =>
            FileIO.save(boardJson)
            complete {
              "ok"
            }
          }
        }
      },
    )
    startHttpServer(routes)
  }

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[?]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("0.0.0.0", 8082).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        val msg     = s"FileIo-Server online at http://${address.getHostString}:${address.getPort}/"
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


