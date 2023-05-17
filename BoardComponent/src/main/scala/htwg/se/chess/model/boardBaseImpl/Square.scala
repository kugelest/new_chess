// package htwg.se.chess.model
// package boardComponent
// package boardBaseImpl
package htwg.se.chess.model.BoardComponent
package boardBaseImpl

import boardBaseImpl.pieces.Piece

case class Square(coord: Coord, piece: Option[Piece]) extends SquareInterface {

  override def toString = {
    piece match {
      case Some(p) => p.char.toString()
      case _       => ""
    }
  }
}

object Square {}
