package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class King(color: PieceColor, char: Char, move_count: Int = 0)
    extends Piece {

  override def getPath(startPos: Coord, endPos: Coord): List[Coord] = {
    List()
  }

  override def copy(color: PieceColor, char: Char, move_count: Int): Piece =
    King(color, char, move_count)

}
