package htwg.se.chess
package model
package FileIOComponent
package fileIoJsonImpl

import BoardComponent.BoardInterface
import BoardComponent.boardBaseImpl.Coord
import BoardComponent.boardBaseImpl.Board
import BoardComponent.boardBaseImpl.Square
// import BoardComponent.boardBaseImpl.SquareExtensions
import BoardComponent.boardBaseImpl.pieces.Piece
import BoardComponent.boardBaseImpl.pieces.PieceType
import BoardComponent.boardBaseImpl.pieces.PieceColor

import play.api.libs.json._
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.Try
import scala.io.Source

class FileIO extends FileIOInterface {

  final val FILE_NAME: String = "board.json"

  override def load: Try[Option[BoardInterface]] = {
    Try {
      // val injector: ScalaInjector = Guice.createInjector(new ChessModule)
      val source: String = Source.fromFile(FILE_NAME).getLines.mkString
      val json: JsValue = Json.parse(source)

      // val board = injector.instance[BoardInterface]
      val turnStr = (json \ "board" \ "turn").as[String]
      val turn = PieceColor.valueOf(turnStr)

      val squares = for {
        i <- 0 until 64
      } yield {
        val file = (json \\ "file")(i).as[String]
        val rank = (json \\ "rank")(i).as[String]
        val piece_type = Try(PieceType.valueOf((json \\ "piece")(i).as[String]))
        val color = Try(PieceColor.valueOf((json \\ "color")(i).as[String]))
        val piece =
          if ((piece_type.isSuccess) && (color.isSuccess)) Option(Piece(piece_type.get, color.get)) else Option.empty
        Square(Coord.valueOf(file + "" + rank), piece)
      }
      Option(Board(squares.toVector, List(), turn))
    }
  }

  override def save(board: BoardInterface): Try[Unit] = {
    import java.io._

    Try {
      val pw = new PrintWriter(new File(FILE_NAME))
      pw.write(Json.prettyPrint(gridToJson(board)))
      pw.close
    }
  }

  def gridToJson(board: BoardInterface) = board.toJson

  override def unbind(): Unit = {}

}
