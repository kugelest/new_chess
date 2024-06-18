package htwg.se.chess
package aview


import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.client.RequestBuilding._
import pekko.http.scaladsl.model.HttpResponse
import pekko.http.scaladsl.unmarshalling.Unmarshal
import pekko.stream.SystemMaterializer
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import spray.json._

object HttpTui {
  implicit val system: ActorSystem[_]                     = ActorSystem(Behaviors.empty, "RootActor")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val client = new RootHttpClient()

  def processCommand(command: String): Unit = {
    command.split(" ").toList match {
      case "create" :: "board" :: Nil                   =>
        client.createBoard().foreach(println)
      case "get" :: "boards" :: Nil                     =>
        client.getBoards().foreach(println)
      case "get" :: "board" :: id :: Nil                =>
        client.getBoard(id.toInt).foreach(println)
      case "board" :: id :: "move" :: from :: to :: Nil =>
        val moveJson = s"""{ "from": "$from", "to": "$to" }"""
        client.execMove(id.toInt, moveJson).foreach(println)
      case "save" :: Nil                     =>
        client.save().foreach(println)
      case _                                            =>
        println("Unknown command")
    }
  }

  def run() = {
    println("Welcome to the TUI. Type your commands:")
    println("----------------------------------------------")
    println("create board")
    println("get boards")
    println("get board [ID]")
    println("board [ID] move [FROM] [TO]")
    println("save")
    println("----------------------------------------------")
    Iterator.continually(StdIn.readLine()).takeWhile(_ != "exit").foreach { command =>
      processCommand(command)
      println("----------------------------------------------")
    }
    system.terminate()
  }
}
