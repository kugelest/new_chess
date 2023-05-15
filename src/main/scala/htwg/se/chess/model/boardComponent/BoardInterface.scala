package htwg.se.chess.model
package boardComponent

trait BoardInterface {
  def turn: PieceColorInterface

  def startPos(): BoardInterface
  def isMoveValid(from: String, to: String): Boolean
}

trait MoveInterface {
  def from: String
  def to: String
}

trait PieceColorInterface {}
