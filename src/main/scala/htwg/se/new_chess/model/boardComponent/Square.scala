package htwg.se.chess.model
package boardComponent

import boardComponent.pieces.Piece

case class Square(coord: Coord, piece: Option[Piece]) {

  override def toString = {
    piece match {
      case Some(p) => p.char.toString()
      case _       => ""
    }
  }
}

object Square {}
