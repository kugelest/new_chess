package htwg.se.chess.model.boardComponent
package pieces

import pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
import pieces.PieceType.*
import pieces.PieceColor.*

trait Piece {
  def color: PieceColor
  def char: Char
  def unmoved: Boolean
  def getPath(startPos: Coord, endPos: Coord): List[Coord]
  // def copy(color: PieceColor = this.color, unmoved: Boolean = this.unmoved): Piece
  def copy(color: PieceColor = color, char: Char = char, unmoved: Boolean = unmoved): Piece
  override def toString() = char.toString
}

object Piece {
  def apply(kind: PieceType, color: PieceColor, unmoved: Boolean): Piece = {
    color.match {
      case WHITE =>
        kind.match {
          case PAWN   => Pawn(WHITE, '♙', unmoved)
          case ROOK   => Rook(WHITE, '♖', unmoved)
          case KNIGHT => Knight(WHITE, '♘', unmoved)
          case BISHOP => Bishop(WHITE, '♗', unmoved)
          case QUEEN  => Queen(WHITE, '♕', unmoved)
          case KING   => King(WHITE, '♔', unmoved)
        }
      case PieceColor.BLACK =>
        kind.match {
          case PAWN   => Pawn(BLACK, '♟', unmoved)
          case ROOK   => Rook(BLACK, '♜', unmoved)
          case KNIGHT => Knight(BLACK, '♞', unmoved)
          case BISHOP => Bishop(BLACK, '♝', unmoved)
          case QUEEN  => Queen(BLACK, '♛', unmoved)
          case KING   => King(BLACK, '♚', unmoved)

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
