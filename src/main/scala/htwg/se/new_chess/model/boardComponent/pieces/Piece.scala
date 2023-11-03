package htwg.se.chess.model.boardComponent
package pieces

import pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
import pieces.PieceType.*
import pieces.PieceColor.*

trait Piece {
  def color: PieceColor
  def move_count: Int
  def worth: Int
  def char: Char
  def getPath(startPos: Coord, endPos: Coord): List[Coord]
  def sightOnEmptyBoard(coord: Coord): List[Coord]
  def copy(color: PieceColor = color, char: Char = char, worth: Int = worth, move_count: Int = move_count): Piece
  override def toString() = char.toString
}

object Piece {
  def apply(kind: PieceType, color: PieceColor): Piece = {
    color.match {
      case WHITE =>
        kind.match {
          case PAWN   => Pawn(WHITE, '♙', 1)
          case KNIGHT => Knight(WHITE, '♘', 3)
          case BISHOP => Bishop(WHITE, '♗', 3)
          case ROOK   => Rook(WHITE, '♖', 5)
          case QUEEN  => Queen(WHITE, '♕', 9)
          case KING   => King(WHITE, '♔', 1000)
        }
      case PieceColor.BLACK =>
        kind.match {
          case PAWN   => Pawn(BLACK, '♟', 1)
          case KNIGHT => Knight(BLACK, '♞', 3)
          case BISHOP => Bishop(BLACK, '♝', 3)
          case ROOK   => Rook(BLACK, '♜', 5)
          case QUEEN  => Queen(BLACK, '♛', 9)
          case KING   => King(BLACK, '♚', 1000)

        }
    }
  }
}

enum PieceType {
  case PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
}

enum PieceColor {
  case WHITE, BLACK
}
