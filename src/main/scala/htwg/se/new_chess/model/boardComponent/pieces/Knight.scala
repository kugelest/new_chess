package htwg.se.new_chess.model.boardComponent.pieces

import htwg.se.new_chess.model.boardComponent.Piece
import htwg.se.new_chess.model.boardComponent.Coord

case class Knight() extends Piece {
  override def isMoveValid(startPos: Coord, endPos: Coord): Boolean = {
    true
  }

}
