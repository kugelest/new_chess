package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor.*
import boardComponent.Coord
import scala.util.Success

case class Pawn(color: PieceColor, char: Char, worth: Int, move_count: Int = 0) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    (color, move_count) match {
      case (WHITE, 0) if start_coord.neighbor(0, 2) == Success(end_coord) =>
        start_coord.upperTwo()
      case (WHITE, _) if start_coord.upperFront().contains(end_coord) =>
        List(end_coord)
      case (BLACK, 0) if start_coord.neighbor(0, -2) == Success(end_coord) =>
        start_coord.lowerTwo()
      case (BLACK, _) if start_coord.lowerFront().contains(end_coord) =>
        List(end_coord)
      case _ => List()
    }
  }

  override def sightOnEmptyBoard(coord: Coord): List[Coord] = {
    val sight = color match {
      case WHITE => coord.upperLeftAndRightNeighbor()
      case BLACK => coord.lowerLeftAndRightNeighbor()
    }
    sight
  }

  override def copy(color: PieceColor, char: Char, worth: Int, move_count: Int): Piece =
    Pawn(color, char, worth, move_count)

}

object Pawn {
  val worth = 1
}


