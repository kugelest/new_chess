package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Rook(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int]) extends Piece {

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

  override def threateningSightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    List(
      coord.upperNeighbors(),
      coord.rightNeighbors(),
      coord.lowerNeighbors(),
      coord.leftNeighbors()
    )
  }

  override def walkingSightOnEmptyBoard(coord: Coord): List[List[Coord]] = threateningSightOnEmptyBoard(coord)

  override def increaseMoveCount(i: Int): Rook = this.copy(move_count = move_count + i)

  override def notation: Char = 'R'
}

object Rook {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val char = if(color == WHITE) '♖' else '♜'
    val worth = 5
    new Rook(color, id, char, worth, move_count, promoted_on_move)
  }
}
