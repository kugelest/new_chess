package htwg.se.chess
package model
package fileIOComponent
package fileIoJsonImpl


import scala.collection.immutable.Map
import play.api.libs.json._
import scala.util.Try
import scala.io.Source

object FileIO {

  final val FILE_NAME: String = "board.json"

  def load: Try[String] = {
    Try {
      Source.fromFile(FILE_NAME).getLines.mkString
    }
  }

  def save(board: String): Try[Unit] = {
    import java.io._

    Try {
      val pw = new PrintWriter(new File(FILE_NAME))
      pw.write(board)
      pw.close
    }
  }

  def unbind(): Unit = {}

}
