package htwg.se.new_chess.model.boardComponent

case class Board(cells: Vector[Cell]) {
  def this() = {
    this(Squares.values.map(Cell(_)).toVector)
  }

  override def toString(): String = {
    cells.map(c => c.square.pos).mkString("")
  }

}
