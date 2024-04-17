package htwg.se.chess
package model
package boardComponent
package boardBaseImpl
package pieces

// import pieces.Piece
// import pieces.PieceColor
import PieceColor._
// import boardComponent.Coord

case class King(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int], is_checked: Boolean) extends Piece {

  override def getPath(from: Coord, to: Coord): List[Coord] = {
    from.surroundingNeighbors().find(_ == to).match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

  override def threateningSightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    coord.surroundingNeighbors().map(List(_))
  }

  override def walkingSightOnEmptyBoard(coord: Coord): List[List[Coord]] = threateningSightOnEmptyBoard(coord)

  override def increaseMoveCount(i: Int): King = this.copy(move_count = move_count + i)

  def setChecked(check: Boolean): King = this.copy(is_checked = check)

  override def notation: Char = 'K'
}

object King {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val char = if(color == WHITE) '♔' else '♚'
    val worth = 1000
    new King(color, id, char, worth, move_count, promoted_on_move, false)
  }
}
