package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Rook(color: PieceColor, char: Char, worth: Int, move_count: Int) extends Piece[RookType] {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction = (
      start_coord.file,
      start_coord.rank,
      end_coord.file,
      end_coord.rank
    ) match {
      case (sx, sy, ex, ey) if (sx == ex) && (sy < ey) =>
        start_coord.upperNeighbors()
      case (sx, sy, ex, ey) if (sx == ex) && (sy > ey) =>
        start_coord.lowerNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy == ey) =>
        start_coord.rightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy == ey) =>
        start_coord.leftNeighbors()
      case _ => List()
    }
    direction.reverse.dropWhile(_ != end_coord)
  }

  override def sightOnEmptyBoard(coord: Coord): List[Coord] = {
    val orthogonalNeighbors = coord.upperNeighbors() ++ coord.rightNeighbors() ++ coord.lowerNeighbors() ++ coord.leftNeighbors()
    orthogonalNeighbors
  }

  override def increaseMoveCount(i: Int): Rook = this.copy(move_count = move_count + i)

}

object Rook {
  def apply(color: PieceColor) = {
    val char = if(color == WHITE) '♖' else '♜'
    new Rook(color, char, 5, 0)
  }
}
