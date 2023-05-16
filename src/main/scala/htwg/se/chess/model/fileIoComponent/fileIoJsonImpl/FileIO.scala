package htwg.se.chess
package model
package fileIoComponent
package fileIoJsonImpl

import boardComponent.BoardInterface
import boardComponent.boardBaseImpl.Coord
import boardComponent.boardBaseImpl.Board
import boardComponent.boardBaseImpl.Square
import boardComponent.boardBaseImpl.SquareExtensions
import boardComponent.boardBaseImpl.pieces.Piece
import boardComponent.boardBaseImpl.pieces.PieceType
import util.PieceColor

import play.api.libs.json._
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.Try
import scala.io.Source

class FileIO extends FileIOInterface {

  final val FILE_NAME: String = "board.json"

  override def load: Try[Option[BoardInterface]] = {
    // var boardOpt: Option[BoardInterface] = None
    Try {
      val source: String = Source.fromFile(FILE_NAME).getLines.mkString

      val json: JsValue = Json.parse(source)
      val injector: ScalaInjector = Guice.createInjector(new ChessModule)

      val board = injector.instance[BoardInterface]

      val turn = PieceColor.valueOf((json \ "board" \ "turn").get.toString)

      val squares = for {
        i <- 0 until 64
      } yield {
        val file = (json \\ "file")(i).as[String]
        val rank = (json \\ "rank")(i).as[String]
        val piece_type = Try(PieceType.valueOf((json \\ "piece")(i).as[String].toUpperCase()))
        val color = Try(PieceColor.valueOf((json \\ "color")(i).as[String]))
        val piece = if (piece_type.isSuccess) Option(Piece(piece_type.get, color.get)) else Option.empty
        Square(Coord.valueOf(file + "" + rank), piece)
      }

      Option(Board(squares.toVector, List(), turn))

      // size match {
      //   case 1 =>
      //     gridOption =
      //       Some(injector.instance[GridInterface](Names.named("tiny")))
      //   case 4 =>
      //     gridOption =
      //       Some(injector.instance[GridInterface](Names.named("small")))
      //   case 9 =>
      //     gridOption =
      //       Some(injector.instance[GridInterface](Names.named("normal")))
      //   case _ =>
      // }
      // boardOpt match {
      //   case Some(board) => {
      //     var _grid = grid
      //     for (index <- 0 until size * size) {
      //       val row = (json \\ "row") (index).as[Int]
      //       val col = (json \\ "col") (index).as[Int]
      //       val cell = (json \\ "cell") (index)
      //       val value = (cell \ "value").as[Int]
      //       _grid = _grid.set(row, col, value)
      //       val given = (cell \ "given").as[Boolean]
      //       val showCandidates = (cell \ "showCandidates").as[Boolean]
      //       if (given) _grid = _grid.setGiven(row, col, value)
      //       if (showCandidates) _grid = _grid.setShowCandidates(row, col)
      //     }
      //     gridOption = Some(_grid)
      //   }
      //   case None =>
      // }
      // Option(board)
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
