package htwg.se.chess.model
package boardComponent
package pieces

// import pieces
// import pieces.Piece
// import pieces.PieceTyp
// import pieces.PieceTyp.PawnType
// import pieces.PieceTyp._
// import pieces.PieceTyp.PawnType
// import pieces.PawnType
// import pieces.PieceColor
import pieces.PieceColor._
import boardComponent.Coord
import scala.util.Success


case class Pawn(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int]) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    (color, move_count) match {
      case (WHITE, 0) if start_coord.neighbor(0, 2) == Success(end_coord) =>
        start_coord.upperTwo()
      case (WHITE, _) if start_coord.upperFront().contains(end_coord) =>
        List(end_coord)
      case (BLACK, 0) if start_coord.neighbor(0, -2) == Success(end_coord) =>
        start_coord.lowerTwo()
      case (BLACK, _) if start_coord.lowerFront().contains(end_coord) =>
        List(end_coord)
      case _ => List()
    }
  }

  override def sightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    val sight = color match {
      case WHITE => List(coord.upperLeftAndRightNeighbor())
      case BLACK => List(coord.lowerLeftAndRightNeighbor())
    }
    sight
  }

  override def increaseMoveCount(i: Int): Pawn = this.copy(move_count = move_count + i)
}

object Pawn {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val worth = 1
    val char = if(color == WHITE) '♙' else '♟'
    new Pawn(color, id, char, worth, move_count, promoted_on_move)
  }
}
