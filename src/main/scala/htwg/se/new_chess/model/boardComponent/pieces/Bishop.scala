package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Bishop(color: PieceColor, char: Char, worth: Int, move_count: Int) extends Piece[Bishop] {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction = (
      start_coord.file,
      start_coord.rank,
      end_coord.file,
      end_coord.rank
    ) match {
      case (sx, sy, ex, ey) if (sx < ex) && (sy < ey) =>
        start_coord.upperRightNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy > ey) =>
        start_coord.lowerRightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy < ey) =>
        start_coord.upperLeftNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy > ey) =>
        start_coord.lowerLeftNeighbors()
      case _ => List()
    }
    direction.reverse.dropWhile(_ != end_coord)
  }

  override def sightOnEmptyBoard(coord: Coord): List[Coord] = {
    val diagonalNeighbors = coord.upperLeftNeighbors() ++ coord.upperRightNeighbors() ++ coord.lowerLeftNeighbors() ++ coord.lowerRightNeighbors()
    diagonalNeighbors
  }

  override def increaseMoveCount(i: Int): Bishop = this.copy(move_count = move_count + i)
}

object Bishop {
  def apply(color: PieceColor) = {
    val char = if(color == WHITE) '♗' else '♝'
    new Bishop(color, char, 3, 0)
  }
}
