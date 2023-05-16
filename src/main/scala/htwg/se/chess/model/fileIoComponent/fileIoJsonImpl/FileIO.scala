package htwg.se.chess
package model
package fileIoComponent
package fileIoJsonImpl

import boardComponent.BoardInterface

import play.api.libs.json._
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._
import scala.util.Try
import scala.io.Source

class FileIO extends FileIOInterface {

  final val FILE_NAME: String = "board.json"

  override def load: Try[Option[BoardInterface]] = {
    var boardOpt: Option[BoardInterface] = None
    Try {
      val source: String = Source.fromFile(FILE_NAME).getLines.mkString

      val json: JsValue = Json.parse(source)
      // val size = (json \ "grid" \ "size").get.toString.toInt
      val injector: ScalaInjector = Guice.createInjector(new ChessModule)

      boardOpt = Some(injector.instance[BoardInterface])

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
      boardOpt
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
