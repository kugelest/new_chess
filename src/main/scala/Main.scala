import htwg.se.new_chess.model.boardComponent.Board
import htwg.se.new_chess.model.boardComponent.Coord.*

@main def hello: Unit =
  val board = Board()
  println(board.startPos())
  println(A1.upperNeighbors())
