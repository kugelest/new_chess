import htwg.se.new_chess.model.boardComponent.Coord
import htwg.se.new_chess.model.boardComponent.Coord.*
import htwg.se.new_chess.model.boardComponent.Board
import htwg.se.new_chess.model.boardComponent.MoveValidator

val coord: Coord = A5
val board: Board = Board().startPos()
val validator: MoveValidator = MoveValidator(board)
Coord.valueOf("A1")

validator.isMoveValid(A1, A2)

A1.upperNeighbors()
B5.rightNeighbors()
H8.rightNeighbors()
