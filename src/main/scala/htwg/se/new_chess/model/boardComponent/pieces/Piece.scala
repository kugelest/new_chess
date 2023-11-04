package htwg.se.chess.model.boardComponent
package pieces

// import pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
// import pieces.PieceColor.*

enum PieceColor {
  case WHITE, BLACK
}

sealed trait PieceTyp
trait PawnType extends PieceTyp
trait KnightType extends PieceTyp
trait BishopType extends PieceTyp
trait RookType extends PieceTyp
trait QueenType extends PieceTyp
trait KingType extends PieceTyp

trait Piece[+PieceTyp <: Piece[PieceTyp]]  {
  def color: PieceColor
  def move_count: Int
  def increaseMoveCount(n: Int): PieceTyp
  def worth: Int
  def char: Char
  def getPath(startPos: Coord, endPos: Coord): List[Coord]
  def sightOnEmptyBoard(coord: Coord): List[Coord]
  override def toString() = char.toString
}

object Piece {
  def apply(pieceType: String, color: PieceColor): Piece[PieceTyp] = {
    pieceType match {
      case "Pawn" => Pawn(color)
      case "Knight" => Knight(color)
      case "Bishop" => Bishop(color)
      case "Rook" => Rook(color)
      case "Queen" => Queen(color)
      case "King" => King(color)
    }
  }
}

