package htwg.se.new_chess.model.boardComponent.pieces

import htwg.se.new_chess.model.boardComponent.Piece
import htwg.se.new_chess.model.boardComponent.PieceColor
import htwg.se.new_chess.model.boardComponent.Coord

case class Bishop(color: PieceColor, char: Char, unmoved: Boolean = true) extends Piece {
  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction = (start_coord.file, start_coord.rank, end_coord.file, end_coord.rank) match {
      case (sx, sy, ex, ey) if (sx < ex) && (sy < ey) => start_coord.upperRightNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy > ey) => start_coord.lowerRightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy < ey) => start_coord.upperLeftNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy > ey) => start_coord.lowerLeftNeighbors()
      case _                                          => List()
    }
    direction.reverse.dropWhile(_ != end_coord)
  }

}
