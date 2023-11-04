package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class Knight(color: PieceColor, char: Char, worth: Int, move_count: Int) extends Piece[KnightType] {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    start_coord.knightNeighbors().find(_ == end_coord) match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

  override def sightOnEmptyBoard(coord: Coord): List[Coord] = {
    coord.knightNeighbors()
  }

  override def increaseMoveCount(i: Int): Knight = this.copy(move_count = move_count + i)
}

object Knight {
  def apply(color: PieceColor) = {
    val char = if(color == WHITE) '♘' else '♞'
    new Knight(color, char, 3, 0)
  }
}
