package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class Knight(color: PieceColor, char: Char, worth: Int, move_count: Int = 0) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    start_coord.knightNeighbors().find(_ == end_coord) match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

  override def copy(color: PieceColor, char: Char, worth: Int, move_count: Int): Piece =
    Knight(color, char, worth, move_count)
}

object Knight {
  def worth: Int = 3
}
