package model
package BoardComponent
package boardBaseImpl
package pieces

import java.util.concurrent.atomic.AtomicInteger

import PieceType._

enum PieceColor {
  case WHITE, BLACK
}

enum PieceType {
  case PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
}

trait Piece {
  def color: PieceColor
  def move_count: Int
  def increaseMoveCount(n: Int): Pawn | Knight | Bishop | Rook | Queen | King
  def worth: Int
  def char: Char
  def getPath(startPos: Coord, endPos: Coord): List[Coord]
  def threateningSightOnEmptyBoard(coord: Coord): List[List[Coord]]
  def walkingSightOnEmptyBoard(coord: Coord): List[List[Coord]]
  def notation: Char
  override def toString()                                       = char.toString
}

object Piece {
  private val idCounter = AtomicInteger(0)

  def generateId(): Int = idCounter.incrementAndGet()

  def apply(pieceType: PieceType, color: PieceColor, move_count: Int = 0, promoted_on_move: Option[Int] = None): Piece = {
    val id = generateId()
    pieceType match {
      case PAWN   => Pawn(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
      case KNIGHT => Knight(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
      case BISHOP => Bishop(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
      case ROOK   => Rook(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
      case QUEEN  => Queen(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
      case KING   => King(color = color, id = id, move_count = move_count, promoted_on_move = promoted_on_move)
    }
  }
}
