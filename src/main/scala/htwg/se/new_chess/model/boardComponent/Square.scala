package htwg.se.chess.model
package boardComponent

import boardComponent.pieces.Piece

case class Square(coord: Coord, piece: Option[Piece]) {}

object Square {}
