package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord

case class King(color: PieceColor, char: Char, worth: Int, move_count: Int) extends Piece {

  override def getPath(from: Coord, to: Coord): List[Coord] = {
    from.surroundingNeighbors().find(_ == to).match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

  override def sightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    List(coord.surroundingNeighbors())
  }

  override def increaseMoveCount(i: Int): King = this.copy(move_count = move_count + i)

}

object King {
  def apply(color: PieceColor) = {
    val char = if(color == WHITE) '♔' else '♚'
    new King(color, char, 1000, 0)
  }
}
