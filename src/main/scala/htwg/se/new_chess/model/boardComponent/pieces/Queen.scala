package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class Queen(color: PieceColor, char: Char, worth: Int, move_count: Int = 0) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction: List[Coord] = (
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
    val orthogonalNeighbors = coord.upperNeighbors() ++ coord.rightNeighbors() ++ coord.lowerNeighbors() ++ coord.leftNeighbors()
    diagonalNeighbors ++ orthogonalNeighbors
  }

  override def copy(color: PieceColor, char: Char, worth: Int, move_count: Int): Piece =
    Queen(color, char, worth, move_count)
}


object Queen {
  val worth = 9
}
