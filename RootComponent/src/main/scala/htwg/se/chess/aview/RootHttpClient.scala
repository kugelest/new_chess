package htwg.se.chess
package aview

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

class RootHttpClient()(implicit system: ActorSystem[_]) {
  import system.executionContext
  import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import DefaultJsonProtocol._

  private implicit val timeout: Timeout = Timeout(5.seconds)

  private val baseUrl = "http://0.0.0.0:8090"

  def getBoards(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"${baseUrl}/boards"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def getBoard(id: Int): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"${baseUrl}/board/${id}"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def createBoard(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(POST, uri = s"${baseUrl}/board/create"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def execMove(id: Int, move: String): Future[String] = {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, move)
    val responseFuture = Http().singleRequest(HttpRequest(PUT, uri = s"${baseUrl}/board/${id}/move", entity = requestEntity))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def save(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"${baseUrl}/save"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def load(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = s"${baseUrl}/load"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }
}
