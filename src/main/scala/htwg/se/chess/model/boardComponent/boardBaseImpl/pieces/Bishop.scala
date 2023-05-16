package htwg.se.chess
package model
package boardComponent
package boardBaseImpl
package pieces

import pieces.Piece
import util.PieceColor
import boardBaseImpl.Coord

case class Bishop(color: PieceColor, char: Char, move_count: Int = 0) extends Piece {
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

  override def copy(color: PieceColor, char: Char, move_count: Int): Piece =
    Bishop(color, char, move_count)
}
