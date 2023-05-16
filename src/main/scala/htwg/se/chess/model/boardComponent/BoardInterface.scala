package htwg.se.chess
package model
package boardComponent

import util.PieceColor
import boardComponent.boardBaseImpl.SquareColors

import play.api.libs.json.JsValue

trait BoardInterface {
  def squares: Vector[SquareInterface]
  def turn: PieceColor

  def startPos(): BoardInterface
  def isMoveValid(from: String, to: String): Boolean
  def doMove(from: String, to: String): BoardInterface
  def undoMove(from: String, to: String): BoardInterface
  def toJson: JsValue
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
