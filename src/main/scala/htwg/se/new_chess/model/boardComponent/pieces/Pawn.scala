package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor.*
import boardComponent.Coord
import scala.util.Success

case class Pawn(color: PieceColor, char: Char, unmoved: Boolean = true) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    (color, unmoved) match {
      case (WHITE, true) if start_coord.neighbor(0, 2) == Success(end_coord)  => start_coord.upperTwo()
      case (WHITE, _) if start_coord.upperFront().contains(end_coord)         => List(end_coord)
      case (BLACK, true) if start_coord.neighbor(0, -2) == Success(end_coord) => start_coord.lowerTwo()
      case (BLACK, _) if start_coord.lowerFront().contains(end_coord)         => List(end_coord)
      case _                                                                  => List()
    }
  }

}
