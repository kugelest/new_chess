package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Knight(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int]) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    start_coord.knightNeighbors().find(_ == end_coord) match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

  override def sightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    List(coord.knightNeighbors())
  }

  override def increaseMoveCount(i: Int): Knight = this.copy(move_count = move_count + i)
}

object Knight {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val char = if(color == WHITE) '♘' else '♞'
    val worth = 3
    new Knight(color, id, char, worth, move_count, promoted_on_move)
  }
}
