package htwg.se.new_chess.model.boardComponent

abstract class Piece {
  def isMoveValid(startPos: Position, endPos: Position): Boolean

}

object Piece {
  def apply(kind: PieceType) = {
    kind.match {
      case PAWN   => Pawn()
      case ROOK   => Rook()
      case KNIGHT => Knight()
      case BISHOP => Bishop()
      case QUEEN  => Queen()
      case KING   => King()
    }

  }

  enum PieceType {
    case PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
  }

}
