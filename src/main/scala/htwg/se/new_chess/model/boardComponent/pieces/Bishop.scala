package htwg.se.new_chess.model.boardComponent.pieces

import htwg.se.new_chess.model.boardComponent.Piece

case class Bishop() extends Piece {
  override def isMoveValid(startPos: Coord, endPos: Position): Boolean = {
    true

  }

}
