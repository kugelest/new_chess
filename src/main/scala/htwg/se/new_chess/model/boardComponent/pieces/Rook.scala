package htwg.se.new_chess.model.boardComponent.pieces

import htwg.se.new_chess.model.boardComponent.Piece
import htwg.se.new_chess.model.boardComponent.PieceColor
import htwg.se.new_chess.model.boardComponent.Coord

case class Rook(color: PieceColor, char: Char, unmoved: Boolean = true) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction = (start_coord.file, start_coord.rank, end_coord.file, end_coord.rank) match {
      case (sx, sy, ex, ey) if (sx == ex) && (sy < ey) => start_coord.upperNeighbors()
      case (sx, sy, ex, ey) if (sx == ex) && (sy > ey) => start_coord.lowerNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy == ey) => start_coord.rightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy == ey) => start_coord.leftNeighbors()
      case _                                           => List()
    }
    direction.reverse.dropWhile(_ != end_coord)
  }

}
