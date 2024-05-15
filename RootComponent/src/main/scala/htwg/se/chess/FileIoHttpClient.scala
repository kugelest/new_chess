package htwg.se.chess

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model._
import org.apache.pekko.http.scaladsl.model.HttpMethods._
import org.apache.pekko.http.scaladsl.unmarshalling.Unmarshal
import org.apache.pekko.util.Timeout
import spray.json._

import scala.concurrent.Future
import scala.concurrent.duration._

class FileIoHttpClient()(implicit system: ActorSystem[_]) {
  import system.executionContext

  private implicit val timeout: Timeout = Timeout(5.seconds)

  def load(): Future[String] = {
    val responseFuture = Http().singleRequest(HttpRequest(GET, uri = "http://0.0.0.0:8082/fileio/load"))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

  def save(boardJson: String): Future[String] = {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, boardJson)
    val responseFuture = Http().singleRequest(HttpRequest(PUT, uri = s"http://0.0.0.0:8082/fileio/save", entity = requestEntity))
    responseFuture.flatMap { response =>
      Unmarshal(response.entity).to[String]
    }
  }

}
