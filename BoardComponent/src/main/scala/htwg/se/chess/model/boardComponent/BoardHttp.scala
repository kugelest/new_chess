package htwg.se.chess
package model
package boardComponent

// import boardComponent.BoardRegistry
// import boardComponent.BoardRoutes


import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.server.Route

import scala.util.Failure
import scala.util.Success
import Console.{GREEN, RED, BLUE, RESET}

object BoardHttp {

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[?]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8081).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        val msg = s"BoardHttp-Server online at http://${address.getHostString}:${address.getPort}/"
        system.log.info(msg)
        // system.log.info("BoardHttp-Server online at http://{}:{}/", address.getHostString, address.getPort)
        Console.println(s"${BLUE}${msg}${RESET}")
      case Failure(ex) =>
        val msg = "Failed to bind HTTP endpoint, terminating system"
        system.log.error(msg, ex)
        Console.err.println(s"${RED}${msg}${RESET}")
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val boardRegistryActor = context.spawn(BoardRegistry(), "BoardRegistryActor")
      context.watch(boardRegistryActor)

      val routes = new BoardRoutes(boardRegistryActor)(context.system)
      startHttpServer(routes.boardRoutes)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "BoardHttp")
  }
}
