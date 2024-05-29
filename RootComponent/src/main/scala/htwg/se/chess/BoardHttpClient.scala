package htwg.se.chess

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model._
import org.apache.pekko.http.scaladsl.model.HttpMethods._
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshal
import org.apache.pekko.util.Timeout
import spray.json._
// import BoardJsonFormats._

import scala.concurrent.Future
import scala.concurrent.duration._

class BoardHttpClient()(implicit system: ActorSystem[_]) {
  import system.executionContext

  private implicit val timeout: Timeout = Timeout(5.seconds)

  def getBoards(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = "http://0.0.0.0:8081/boards"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def getBoardsStr(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = "http://0.0.0.0:8081/boards/string"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def getBoard(id: Int): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"http://0.0.0.0:8081/board/$id"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def getBoardStr(id: Int): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"http://0.0.0.0:8081/board/$id/string"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def createBoard(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(POST, uri = "http://0.0.0.0:8081/board/create"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def execMove(id: Int, move: String): Future[String] = {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, move)
    val responseFuture = Http().singleRequest(HttpRequest(PUT, uri = s"http://0.0.0.0:8081/board/$id/move", entity = requestEntity))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }
}
