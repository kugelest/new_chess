package htwg.se.chess.model.boardComponent
package pieces

import pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
import pieces.PieceType.*
import pieces.PieceColor.*

trait Piece {
  def color: PieceColor
  def char: Char
  def move_count: Int
  def getPath(startPos: Coord, endPos: Coord): List[Coord]
  def copy(color: PieceColor = color, char: Char = char, move_count: Int = move_count): Piece
  override def toString() = char.toString
}

object Piece {
  def apply(kind: PieceType, color: PieceColor): Piece = {
    color.match {
      case WHITE =>
        kind.match {
          case PAWN   => Pawn(WHITE, '♙')
          case ROOK   => Rook(WHITE, '♖')
          case KNIGHT => Knight(WHITE, '♘')
          case BISHOP => Bishop(WHITE, '♗')
          case QUEEN  => Queen(WHITE, '♕')
          case KING   => King(WHITE, '♔')
        }
      case PieceColor.BLACK =>
        kind.match {
          case PAWN   => Pawn(BLACK, '♟')
          case ROOK   => Rook(BLACK, '♜')
          case KNIGHT => Knight(BLACK, '♞')
          case BISHOP => Bishop(BLACK, '♝')
          case QUEEN  => Queen(BLACK, '♛')
          case KING   => King(BLACK, '♚')

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
