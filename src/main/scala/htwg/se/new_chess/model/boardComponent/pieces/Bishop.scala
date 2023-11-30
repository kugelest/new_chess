package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Bishop(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int]) extends Piece {

  override def getPath(from: Coord, to: Coord): List[Coord] = {
    val direction = (
      from.file,
      from.rank,
      to.file,
      to.rank
    ) match {
      case (sx, sy, ex, ey) if (sx < ex) && (sy < ey) =>
        from.upperRightNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy > ey) =>
        from.lowerRightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy < ey) =>
        from.upperLeftNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy > ey) =>
        from.lowerLeftNeighbors()
      case _ => List()
    }
    direction.reverse.dropWhile(_ != to)
  }

  override def threateningSightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    List(
      coord.upperLeftNeighbors(),
      coord.upperRightNeighbors(),
      coord.lowerLeftNeighbors(),
      coord.lowerRightNeighbors()
    )
  }

  override def walkingSightOnEmptyBoard(coord: Coord): List[List[Coord]] = threateningSightOnEmptyBoard(coord)

  override def increaseMoveCount(i: Int): Bishop = this.copy(move_count = move_count + i)
}

object Bishop {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val char = if(color == WHITE) '♗' else '♝'
    val worth = 3
    new Bishop(color, id, char, worth, move_count, promoted_on_move)
  }
}
