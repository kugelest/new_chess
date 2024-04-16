package htwg.se.chess.model
package BoardComponent
package boardBaseImpl
package pieces

// import pieces.Piece
// import pieces.PieceColor
import PieceColor._
// import boardComponent.Coord

case class Queen(color: PieceColor, id: Int, char: Char, worth: Int, move_count: Int, promoted_on_move: Option[Int]) extends Piece {

  override def getPath(start_coord: Coord, end_coord: Coord): List[Coord] = {
    val direction: List[Coord] = (
      start_coord.file,
      start_coord.rank,
      end_coord.file,
      end_coord.rank
    ) match {
      case (sx, sy, ex, ey) if (sx == ex) && (sy < ey) =>
        start_coord.upperNeighbors()
      case (sx, sy, ex, ey) if (sx == ex) && (sy > ey) =>
        start_coord.lowerNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy == ey) =>
        start_coord.rightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy == ey) =>
        start_coord.leftNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy < ey) =>
        start_coord.upperRightNeighbors()
      case (sx, sy, ex, ey) if (sx < ex) && (sy > ey) =>
        start_coord.lowerRightNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy < ey) =>
        start_coord.upperLeftNeighbors()
      case (sx, sy, ex, ey) if (sx > ex) && (sy > ey) =>
        start_coord.lowerLeftNeighbors()
      case _ => List()
    }
    direction.reverse.dropWhile(_ != end_coord)
  }

  override def threateningSightOnEmptyBoard(coord: Coord): List[List[Coord]] = {
    List(
      coord.upperLeftNeighbors(),
      coord.upperRightNeighbors(),
      coord.lowerLeftNeighbors(),
      coord.lowerRightNeighbors(),
      coord.upperNeighbors(),
      coord.rightNeighbors(),
      coord.lowerNeighbors(),
      coord.leftNeighbors()
    )
  }

  override def walkingSightOnEmptyBoard(coord: Coord): List[List[Coord]] = threateningSightOnEmptyBoard(coord)

  override def increaseMoveCount(i: Int): Queen = this.copy(move_count = move_count + i)

  override def notation: Char = 'Q'
}

object Queen {
  def apply(color: PieceColor, id: Int, move_count: Int, promoted_on_move: Option[Int]) = {
    val char = if(color == WHITE) '♕' else '♛'
    val worth = 9
    new Queen(color, id, char, worth, move_count, promoted_on_move)
  }
}
