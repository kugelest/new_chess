package htwg.se.new_chess.model.boardComponent

case class Board(cells: Vector[Square]) {

  def isMoveValid(startPos: Coord, endPos: Coord): Boolean = {
    val pieceOpt: Option[Piece] = cells.find(_.coord == startPos).flatMap(_.piece)
    pieceOpt match {
      case Some(piece) => piece.isMoveValid(startPos, endPos)
      case None        => false // No piece at the starting position
    }
  }

  override def toString(): String = {
    this.cells
      .sortBy(_.coord.print_ord)
      .map(_.coord.toString)
      .grouped(Coord.len)
      .map(_.mkString(" "))
      .mkString("\n")
  }
}

object Board {
  def apply() = {
    new Board(Coord.values.map(Square(_, None)).toVector)
  }
}
