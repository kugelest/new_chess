package htwg.se.chess.model
package boardComponent
package pieces

import pieces.Piece
import pieces.PieceColor
import boardComponent.Coord

case class Knight(color: PieceColor, char: Char, unmoved: Boolean = true) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    start_coord.knightNeighbors().find(_ == end_coord) match {
      case Some(dest) => List(dest)
      case _          => List()
    }
  }

}
