package htwg.se.new_chess.model.boardComponent

case class Board(cells: Vector[Cell]) {

  override def toString(): String = {
    this.cells
      .sortBy(_.square.print_ord)
      .map(_.square.toString)
      .grouped(Squares.len)
      .map(_.mkString(" "))
      .mkString("\n")
    // this.cells.map(c => c.square.toString).mkString(" ")
  }
}

object Board {
  def apply() = {
    new Board(Squares.values.map(Cell(_)).toVector)
  }

}
