package htwg.se.chess

import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.server.Route

import scala.util.Failure
import scala.util.Success

object ChessHttp {
  // private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
  //   import system.executionContext
  //
  //   val futureBinding = Http().newServerAt("localhost", 8081).bind(routes)
  //   futureBinding.onComplete {
  //     case Success(binding) =>
  //       val address = binding.localAddress
  //       system.log.info("BoardHttp-Server online at http://{}:{}/", address.getHostString, address.getPort)
  //     case Failure(ex) =>
  //       system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
  //       system.terminate()
  //   }
  // }
  //
  def main(args: Array[String]): Unit = {
  //   val rootBehavior = Behaviors.setup[Nothing] { context =>
  //     val boardRegistryActor = context.spawn(BoardRegistry(), "BoardRegistryActor")
  //     context.watch(boardRegistryActor)
  //
  //     val routes = new BoardRoutes(boardRegistryActor)(context.system)
  //     startHttpServer(routes.boardRoutes)(context.system)
  //
  //     Behaviors.empty
  //   }
  //   val system = ActorSystem[Nothing](rootBehavior, "BoardHttp")
    println("hi")
  }
}
