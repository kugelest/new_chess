package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Bishop(color: PieceColor, char: Char, worth: Int, move_count: Int) extends Piece {

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

  override def sightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    print("\nUpperRightNeighbors Bishop:" + coord.upperRightNeighbors())
    List(
      coord.upperLeftNeighbors(),
      coord.upperRightNeighbors(),
      coord.lowerLeftNeighbors(),
      coord.lowerRightNeighbors()
    )
  }

  override def increaseMoveCount(i: Int): Bishop = this.copy(move_count = move_count + i)
}

object Bishop {
  def apply(color: PieceColor) = {
    val char = if(color == WHITE) '♗' else '♝'
    new Bishop(color, char, 3, 0)
  }
}
