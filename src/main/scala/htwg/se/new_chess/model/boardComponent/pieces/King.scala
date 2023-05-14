package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class King(color: PieceColor, char: Char, unmoved: Boolean = true) extends Piece {

  override def getPath(startPos: Coord, endPos: Coord): List[Coord] = {
    List()
  }

}
