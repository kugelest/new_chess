package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class Rook(color: PieceColor, char: Char, move_count: Int = 0)
    extends Piece {

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

  override def copy(color: PieceColor, char: Char, move_count: Int): Piece =
    Rook(color, char, move_count)
}
