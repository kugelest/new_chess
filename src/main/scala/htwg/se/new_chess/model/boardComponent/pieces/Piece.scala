package htwg.se.chess.model.boardComponent
package pieces

// import pieces.{Pawn, Rook, Knight, Bishop, Queen, King}
// import pieces.PieceColor.*


trait Piece[PieceTyp <: Piece[PieceTyp]]  {
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
  def apply(pieceType: String, color: PieceColor): Piece[_] = {
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

// object Piece {
//   def apply(kind: PieceType, color: PieceColor): Piece[_] = {
//     color.match {
//       case WHITE =>
//         kind.match {
//           case PAWN   => Pawn(WHITE, '♙', 1)
//           case KNIGHT => Knight(WHITE, '♘', 3)
//           case BISHOP => Bishop(WHITE, '♗', 3)
//           case ROOK   => Rook(WHITE, '♖', 5)
//           case QUEEN  => Queen(WHITE, '♕', 9)
//           case KING   => King(WHITE, '♔', 1000)
//         }
//       case PieceColor.BLACK =>
//         kind.match {
//           case PAWN   => Pawn(BLACK, '♟', 1)
//           case KNIGHT => Knight(BLACK, '♞', 3)
//           case BISHOP => Bishop(BLACK, '♝', 3)
//           case ROOK   => Rook(BLACK, '♜', 5)
//           case QUEEN  => Queen(BLACK, '♛', 9)
//           case KING   => King(BLACK, '♚', 1000)
//
//         }
//     }
//   }
// }


enum PieceColor {
  case WHITE, BLACK
}
