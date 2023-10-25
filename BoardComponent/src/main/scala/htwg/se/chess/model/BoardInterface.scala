package htwg.se.chess
package model
package BoardComponent

import BoardComponent.boardBaseImpl.pieces.PieceColor
import BoardComponent.boardBaseImpl.SquareColors


trait BoardInterface {
  def squares: Vector[SquareInterface]
  def turn: PieceColor

  def startPos(): BoardInterface
  def isMoveValid(from: String, to: String): Boolean
  def doMove(from: String, to: String): BoardInterface
  def undoMove(from: String, to: String): BoardInterface
  def toHtml(): String
}

trait MoveInterface {
  def from: String
  def to: String
}

trait SquareInterface {
  def coord: CoordInterface
}

trait CoordInterface {
  def print_ord: Int
  def color: SquareColors
}
