package htwg.se.new_chess.model.boardComponent.pieces

import htwg.se.new_chess.model.boardComponent.Piece
import htwg.se.new_chess.model.boardComponent.PieceColor
import htwg.se.new_chess.model.boardComponent.Coord

case class Bishop(color: PieceColor, char: Char) extends Piece {
  override def getPath(startPos: Coord, endPos: Coord): List[Coord] = {
    List()
  }

}
